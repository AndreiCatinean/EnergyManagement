'use client';
import {FormEvent, useEffect, useState} from 'react';
import './styles.css';
import {UserDTO} from "@/app/models/UserDTO";
import {NewUserDTO} from "@/app/models/NewUserDTO";


interface ModalProps {
    isOpen: boolean
    handleFormSubmitAdd: (user: NewUserDTO) => void
    handleFormSubmitUpdate: (user: UserDTO) => void
    onClose: () => void
    addUser: boolean
    userToUpdate: UserDTO | null
}

const UserModal = ({
                       isOpen,
                       handleFormSubmitAdd,
                       handleFormSubmitUpdate,
                       onClose,
                       addUser,
                       userToUpdate
                   }: ModalProps) => {
    const [username, setUsername] = useState('');
    const [isAdmin, setIsAdmin] = useState(false);
    const [password, setPassword] = useState('');

    const refreshFields = () => {
        setUsername('')
        setPassword('')
        setIsAdmin(false)
    }

    useEffect(() => {
        if (isOpen && !addUser && userToUpdate) {
            setUsername(userToUpdate.username);
            setIsAdmin(userToUpdate.isAdmin);
        } else {
            refreshFields();
        }
    }, [addUser, isOpen, userToUpdate]);

    if (!isOpen) return null;


    const closeModal = () => {
        refreshFields()
        onClose()
    }

    const handleSubmit = (event: FormEvent) => {
        event.preventDefault();
        if (addUser)
            handleFormSubmitAdd({username: username, isAdmin: isAdmin, password: password})
        else {
            const userId = userToUpdate ? userToUpdate.id : null
            if (userId)
                handleFormSubmitUpdate({id: userId, isAdmin: isAdmin, username: username})
        }

        refreshFields()
        onClose();
    }


    return (
        <div className="modal-overlay">
            <div className="modal-content">
                <form onSubmit={handleSubmit}>
                    <div>
                        <label>
                            Username:
                            <input
                                type="text"
                                value={username}
                                onChange={(e) => setUsername(e.target.value)}
                                required
                            />
                        </label>
                    </div>
                    {addUser && (
                        <div>
                            <label>
                                Password:
                                <input
                                    type="password"
                                    value={password}
                                    onChange={(e) => setPassword(e.target.value)}
                                    required
                                />
                            </label>
                        </div>
                    )}
                    <div>
                        <label>
                            Is Admin:
                            <input
                                disabled={!addUser}
                                type="checkbox"
                                checked={isAdmin}
                                onChange={(e) => setIsAdmin(e.target.checked)}
                            />
                        </label>
                    </div>
                    <div className="modal-buttons">
                        <button type="submit">{addUser ? "Add User" : "Update User"}</button>
                        <button type="button" onClick={closeModal} className="cancel-button">
                            Cancel
                        </button>
                    </div>
                </form>
            </div>
        </div>
    );
};
export default UserModal;
