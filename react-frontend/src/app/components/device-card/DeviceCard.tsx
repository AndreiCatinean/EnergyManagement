'use client';
import {DeviceDTO} from "@/app/models/DeviceDTO";
import DeveloperBoardIcon from '@mui/icons-material/DeveloperBoard';
import DeleteIcon from '@mui/icons-material/Delete';
import {deleteDevice} from "@/app/components/device-card/api";
import EditIcon from '@mui/icons-material/Edit';
import './styles.css';
import {UserDTO} from "@/app/models/UserDTO";
import PortraitIcon from '@mui/icons-material/Portrait';
import {usePathname, useRouter} from "next/navigation";

interface DeviceCardProps {
    device: DeviceDTO;
    owner: UserDTO | null;
    fetchDevices?: () => void;
    onEdit?: () => void;
    socketMessage?: string;
}

const DeviceCard = ({device, owner, fetchDevices, onEdit, socketMessage}: DeviceCardProps) => {

    const pathname = usePathname()
    const router = useRouter();

    const deletePressedDevice = (deviceId: string) => {
        deleteDevice(deviceId)
            .then(() => {
                if (fetchDevices) {
                    fetchDevices();
                }
            })
            .catch(error => {
                alert("Error deleting the device: " + error);
            });
    };

    const handleCardClick = () => {
        router.push(`/device/${device.id}`);
    };

    return (
        <div className="device-card">
            <div className="device-icon">
                <DeveloperBoardIcon  onClick={handleCardClick}/>
            </div>
            <div className="device-info">
                <h3>{device.description}</h3>
                <p>Address: {device.address}</p>
                <p>Maximum consumption/h: {device.hourlyConsumption}</p>
                <br/>
                {pathname === "/personal-devices" && (
                    <div
                        className={`websocket-message ${socketMessage?.toLowerCase().includes("warning") ? "bad-status" : "good-status"}`}
                    >
                        {socketMessage || "No notifications yet."}
                    </div>
                )}
                {pathname !== '/personal-devices' && (
                    <>
                        <p><PortraitIcon style={{paddingRight: '5%'}}/>{owner ? owner.username : "No owner"}</p>
                        <br/>
                        <br/>
                        <EditIcon
                            style={{marginRight: '22%', cursor: 'pointer'}}
                            onClick={onEdit}
                        />
                        <DeleteIcon
                            onClick={() => deletePressedDevice(device.id)}
                            style={{cursor: 'pointer'}}
                        />
                    </>
                )}
            </div>
        </div>
    );
};

export default DeviceCard;
