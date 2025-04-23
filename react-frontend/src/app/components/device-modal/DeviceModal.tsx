'use client';

import {FormEvent, useEffect, useState} from 'react';
import '@/app/components/user-modal/styles.css';
import {DeviceDTO} from "@/app/models/DeviceDTO";
import {UserDTO} from "@/app/models/UserDTO";
import {NewDeviceDTO} from "@/app/models/NewDeviceDTO";

interface ModalProps {
    isOpen: boolean;
    handleFormSubmitAdd: (device: NewDeviceDTO) => void;
    handleFormSubmitUpdate: (device: DeviceDTO) => void;
    onClose: () => void;
    addDevice: boolean;
    deviceToUpdate?: DeviceDTO | null;
    users: UserDTO[];
}

const DeviceModal = ({
                         isOpen,
                         handleFormSubmitAdd,
                         handleFormSubmitUpdate,
                         onClose,
                         addDevice,
                         deviceToUpdate,
                         users
                     }: ModalProps) => {
    const [description, setDescription] = useState('');
    const [address, setAddress] = useState('');
    const [hourlyConsumption, setHourlyConsumption] = useState('');
    const [ownerId, setOwnerId] = useState<string | null>(null);

    const refreshFields = () => {
        setDescription('');
        setAddress('');
        setHourlyConsumption('');
        setOwnerId(null);
    };

    useEffect(() => {
        if (isOpen && !addDevice && deviceToUpdate) {
            setDescription(deviceToUpdate.description);
            setAddress(deviceToUpdate.address);
            setHourlyConsumption(deviceToUpdate.hourlyConsumption.toString());
            setOwnerId(deviceToUpdate.ownerId || null);
        } else {
            refreshFields();
        }
    }, [addDevice, isOpen, deviceToUpdate]);

    if (!isOpen) return null;

    const closeModal = () => {
        refreshFields();
        onClose();
    };

    const handleSubmit = (event: FormEvent) => {
        event.preventDefault();
        const deviceData = {
            description,
            address,
            hourlyConsumption: parseFloat(hourlyConsumption),
            ownerId: ownerId || null
        };

        if (addDevice) {
            handleFormSubmitAdd(deviceData);
        } else {
            if (deviceToUpdate)
                handleFormSubmitUpdate({...deviceData, id: deviceToUpdate.id});
        }

        refreshFields();
        onClose();
    };

    return (
        <div className="modal-overlay">
            <div className="modal-content">
                <form onSubmit={handleSubmit}>
                    <div>
                        <label>
                            Description:
                            <input
                                type="text"
                                value={description}
                                onChange={(e) => setDescription(e.target.value)}
                                required
                            />
                        </label>
                    </div>
                    <div>
                        <label>
                            Address:
                            <input
                                type="text"
                                value={address}
                                onChange={(e) => setAddress(e.target.value)}
                                required
                            />
                        </label>
                    </div>
                    <div>
                        <label>
                            Consumption/h:
                            <input
                                type="number"
                                value={hourlyConsumption}
                                onChange={(e) => setHourlyConsumption(e.target.value)}
                                required
                            />
                        </label>
                    </div>
                    <div>
                        <label>
                            Owner:
                            <select
                                value={ownerId || ''}
                                onChange={(e) => setOwnerId(e.target.value || null)}
                            >
                                <option value="">No Owner</option>
                                {users.map(user => (
                                    <option key={user.id} value={user.id}>
                                        {user.username}
                                    </option>
                                ))}
                            </select>
                        </label>
                    </div>
                    <div className="modal-buttons">
                        <button type="submit">{addDevice ? "Add Device" : "Update Device"}</button>
                        <button type="button" onClick={closeModal} className="cancel-button">
                            Cancel
                        </button>
                    </div>
                </form>
            </div>
        </div>
    );
};

export default DeviceModal;
