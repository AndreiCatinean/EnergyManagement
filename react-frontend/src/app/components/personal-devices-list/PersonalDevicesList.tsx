'use client';

import {DeviceDTO} from "@/app/models/DeviceDTO";
import DeviceCard from "@/app/components/device-card/DeviceCard";
import '@/app/components/devices-list/styles.css';
import WebSocketDeviceNotificationClient from "@/app/components/device-web-socket/WebSocketDeviceNotificationClient";
import {useState} from "react";

interface PersonalDevicesListProps {
    devices: DeviceDTO[];
}

const PersonalDevicesList = ({devices}: PersonalDevicesListProps) => {

    const [deviceMessages, setDeviceMessages] = useState<{ [key: string]: string }>({});

    const updateMessage = (deviceId: string, message: string) => {
        setDeviceMessages((prevMessages) => ({
            ...prevMessages,
            [deviceId]: message,
        }));
    };

    return (
        <div className="devices-container">
            <h2>Personal Devices</h2>
            <div className="devices-list">
                {devices.length > 0 ? (
                    devices.map((device) => {
                        return (
                            <div key={device.id}>
                                <DeviceCard
                                    device={device}
                                    owner={null}
                                    socketMessage={deviceMessages[device.id] || "No notifications yet."}
                                />
                                <WebSocketDeviceNotificationClient
                                    deviceId={device.id}
                                    onMessage={(msg) => updateMessage(device.id, msg)}
                                />
                            </div>
                        );
                    })
                ) : (
                    <p>No devices found.</p>
                )}
            </div>
        </div>
    );
};

export default PersonalDevicesList;
