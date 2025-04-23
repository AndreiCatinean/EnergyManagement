'use client';


import {usePathname, useRouter} from "next/navigation";
import {useEffect, useState} from "react";
import {determineAuthorization} from "@/app/helper/PermissionHelper";
import Navbar from "@/app/components/navbar/Navbar";
import {DeviceDTO} from "@/app/models/DeviceDTO";
import {getDevicesForUser} from "@/app/personal-devices/api";
import './styles.css';
import PersonalDevicesList from "@/app/components/personal-devices-list/PersonalDevicesList";
import Loading from "@/app/components/loading/Loading";

const PersonalDevices = () => {

    const router = useRouter()
    const pathname = usePathname()
    const [devices, setDevices] = useState<DeviceDTO[]>([]);
    const [loading, setLoading] = useState<boolean>(true)

    useEffect(() => {
        const fetchDevicesForAuthorizedUser = () => {
            setLoading(true);
            determineAuthorization(router, pathname)
                .then((finishedAuthCheck) => {
                    if (finishedAuthCheck) {
                        return getDevicesForUser();
                    }
                })
                .then((devices) => {
                    if (devices) {
                        setDevices(devices);
                    }
                })
                .catch((error: any) => {
                    console.error("Error fetching devices:", error);
                    alert("Error fetching devices: " + error.message);
                })
                .finally(() => {
                    setLoading(false);
                });
        };

        fetchDevicesForAuthorizedUser();
    }, [router, pathname]);

    return (
        <>{
            loading ? <Loading/> :
                <>
                    <Navbar notLogin={true}></Navbar>
                    <br/>
                    <div className={'devices'}>
                        <PersonalDevicesList devices={devices}/>
                    </div>
                </>
        }
        </>
    )
}

export default PersonalDevices
