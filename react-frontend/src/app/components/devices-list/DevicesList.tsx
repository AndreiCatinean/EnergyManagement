'use client';

import {DeviceDTO} from "@/app/models/DeviceDTO";
import DeviceCard from "@/app/components/device-card/DeviceCard";
import './styles.css';
import {UserDTO} from "@/app/models/UserDTO";
import AddCircleOutlineIcon from '@mui/icons-material/AddCircleOutline';
import DeviceModal from "@/app/components/device-modal/DeviceModal";
import {NewDeviceDTO} from "@/app/models/NewDeviceDTO";
import {addDevice, updateDevice} from "@/app/admin-dashboard/api";
import {useState} from "react";

interface DevicesListProps {
    devices: DeviceDTO[];
    users: UserDTO[];
    fetchDevices?: () => void;
}

const DevicesList = ({devices, users, fetchDevices}: DevicesListProps) => {
    const [isModalOpen, setModalOpen] = useState(false);
    const [deviceToUpdate, setDeviceToUpdate] = useState<DeviceDTO | null>(null);
    const [toAddDevice, setToAddDevice] = useState(true);

    const getOwner = (ownerId: string | null) => {
        return users.find(user => user.id === ownerId) || null;
    };

    const handleAddDevice = (newDevice: NewDeviceDTO) => {
        addDevice(newDevice)
            .then(fetchDevices)
            .catch(reason => alert(reason));
        setModalOpen(false);
    };

    const handleUpdateDevice = (device: DeviceDTO) => {
        updateDevice(device)
            .then(fetchDevices)
            .catch(reason => alert(reason));
        setModalOpen(false);
        setDeviceToUpdate(null);
    };

    return (
        <>
            <div className="devices-container">
                <h2>
                    Devices
                    <AddCircleOutlineIcon
                        style={{cursor: 'pointer', marginLeft: '0.5%'}}
                        onClick={() => {
                            setToAddDevice(true);
                            setModalOpen(true);
                        }}
                    />
                </h2>
                <div className="devices-list">
                    {devices.length > 0 ? (
                        devices.map(device => {
                            const owner = device.ownerId ? getOwner(device.ownerId) : null;
                            return (
                                <DeviceCard
                                    key={device.id}
                                    device={device}
                                    owner={owner}
                                    fetchDevices={fetchDevices}
                                    onEdit={() => {
                                        setDeviceToUpdate(device);
                                        setToAddDevice(false);
                                        setModalOpen(true);
                                    }}
                                />
                            );
                        })
                    ) : (
                        <p>No devices found.</p>
                    )}
                </div>
            </div>
            <DeviceModal
                isOpen={isModalOpen}
                onClose={() => {
                    setModalOpen(false);
                    setDeviceToUpdate(null);
                }}
                addDevice={toAddDevice}
                handleFormSubmitAdd={handleAddDevice}
                handleFormSubmitUpdate={handleUpdateDevice}
                deviceToUpdate={deviceToUpdate}
                users={users}
            />
        </>
    );
};

export default DevicesList;
