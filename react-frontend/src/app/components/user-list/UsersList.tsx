'use client';

import React, { useEffect, useState } from "react";
import { UserDTO } from "@/app/models/UserDTO";
import './styles.css';
import { detLoggedUserDeleted, isUserAdmin } from "@/app/helper/PermissionHelper";
import PersonIcon from '@mui/icons-material/Person';
import EditIcon from "@mui/icons-material/Edit";
import DeleteIcon from "@mui/icons-material/Delete";
import { deleteUser } from "@/app/components/user-list/api";
import VerifiedUserIcon from '@mui/icons-material/VerifiedUser';
import PersonAddAltIcon from '@mui/icons-material/PersonAddAlt';
import UserModal from "@/app/components/user-modal/UserModal";
import { NewUserDTO } from "@/app/models/NewUserDTO";
import { addUser, updateUser } from "@/app/admin-dashboard/api";

interface UsersListProps {
    users: UserDTO[];
    fetchUsers: () => void;
    router: any;
}

const UsersList = ({ users, fetchUsers, router }: UsersListProps) => {

    const [isModalOpen, setModalOpen] = useState(false);
    const [userToUpdate, setUserToUpdate] = useState<UserDTO | null>(null);
    const [toAddUser, setToAddUser] = useState(true);
    const [isAdmin, setIsAdmin] = useState(false);

    useEffect(() => {
        isUserAdmin().then(value => value? setIsAdmin(value) : setIsAdmin(false)).catch(error => console.error('Error checking admin status:', error));
    }, []);

    const deletePressedUser = (userId: string) => {
        deleteUser(userId)
            .then(() => {
                fetchUsers();
                detLoggedUserDeleted(userId, router).then(() => { })
            })
            .catch(error => {
                alert("Error deleting the user:" + error);
            });
    };

    const handleAddUser = (newUser: NewUserDTO) => {
        addUser(newUser)
            .then(fetchUsers)
            .catch((reason) => alert(reason))
        setModalOpen(false)
    };

    const handleUpdateUser = (user: UserDTO) => {
        updateUser(user)
            .then(fetchUsers)
            .catch((reason) => alert(reason))
        setModalOpen(false)
        setUserToUpdate(null)
    };

    return (
        <>
            <div className="users-container">
                <h2>
                    Users
                    <PersonAddAltIcon
                        style={{ cursor: 'pointer', marginLeft: '2%' }}
                        onClick={() => {
                            setToAddUser(true);
                            setModalOpen(true);
                        }}
                    />
                </h2>
                <div className="users-list">
                    {users.length > 0 ? (
                        users.map((user) => (
                            <div key={user.id} className="user-item">
                                <h2>{user.username} {user.isAdmin ? <VerifiedUserIcon /> : <PersonIcon />}</h2>
                                {isAdmin && (
                                    <>
                                        <EditIcon style={{ marginRight: '5%', cursor: 'pointer' }}
                                                  onClick={() => {
                                                      setUserToUpdate(user);
                                                      setToAddUser(false);
                                                      setModalOpen(true)
                                                  }}
                                        />
                                        <DeleteIcon
                                            onClick={() => deletePressedUser(user.id)}
                                            style={{ cursor: 'pointer' }}
                                        />
                                    </>
                                )}
                            </div>
                        ))
                    ) : (
                        <p>No users found.</p>
                    )}
                </div>
            </div>
            <UserModal isOpen={isModalOpen} onClose={() => {
                setModalOpen(false);
                setUserToUpdate(null)
            }}
                       addUser={toAddUser} handleFormSubmitAdd={handleAddUser} handleFormSubmitUpdate={handleUpdateUser}
                       userToUpdate={userToUpdate} />
        </>
    );
};

export default UsersList;
