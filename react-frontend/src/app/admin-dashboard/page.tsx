'use client';

import {useEffect, useState} from "react";
import {determineAuthorization} from "@/app/helper/PermissionHelper";
import {usePathname, useRouter} from "next/navigation";
import Navbar from "@/app/components/navbar/Navbar";
import {DeviceDTO} from "@/app/models/DeviceDTO";
import {UserDTO} from "@/app/models/UserDTO";
import './styles.css';
import {getDevices, getUsers} from "@/app/admin-dashboard/api";
import UsersList from "@/app/components/user-list/UsersList";
import DevicesList from "@/app/components/devices-list/DevicesList";
import Loading from "@/app/components/loading/Loading";


const AdminDashboard = () => {
    const router = useRouter();
    const pathname = usePathname()
    const [devices, setDevices] = useState<DeviceDTO[]>([]);
    const [users, setUsers] = useState<UserDTO[]>([]);
    const [loading, setLoading] = useState<boolean>(true)
    const [stillFetchingDevices, setStillFetchingDevices] = useState<boolean>(true)
    const [stillFetchingUsers, setStillFetchingUsers] = useState<boolean>(true)

    useEffect(() => {
        determineAuthorization(router, pathname)
            .then(finishedAuthorizationCheck => {
                if (finishedAuthorizationCheck) {
                    fetchDevices();
                    fetchUsers();
                }
            })
            .catch(error => {
                console.error("Error during authorization or data fetch:", error);
            });

    }, [router, pathname]);


    useEffect(() => {
        if (!stillFetchingDevices && !stillFetchingUsers)
            setLoading(false)
        else
            setLoading(true)

    }, [stillFetchingDevices, stillFetchingUsers]);

    const fetchDevices = () => {
        setStillFetchingDevices(true)
        getDevices()
            .then(response => {
                setDevices(response.data);
            })
            .catch(error => {
                alert("Error fetching devices" + error);
            })
            .finally(() => setStillFetchingDevices(false));
    };

    const fetchUsers = () => {
        setStillFetchingUsers(true)
        getUsers()
            .then(response => {
                setUsers(response.data);
            })
            .catch(error => {
                alert("Error fetching users" + error);
            })
            .finally(() => setStillFetchingUsers(false));
    };

    return (
        <>
            {
                loading ?
                    <Loading/> :
                    <> <Navbar notLogin={true}></Navbar>
                        <div className="admin-dashboard">
                            <UsersList users={users} fetchUsers={fetchUsers} router={router}/>
                            <DevicesList
                                devices={devices}
                                users={users.filter(user => !user.isAdmin)}
                                fetchDevices={fetchDevices}
                            /></div>
                    </>
            }
        </>
    );
};

export default AdminDashboard;
