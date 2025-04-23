
import {AppConstants} from "@/app/helper/AppConstants";
import {DeviceDTO} from "@/app/models/DeviceDTO";
import {getLoggedUser} from "@/app/helper/PermissionHelper";
import {apiCall} from "@/app/api/ApiCall";

export const getDevicesForUser = async (): Promise<DeviceDTO[] | null> => {
    try {
        const loggedUser = await getLoggedUser();
        if (loggedUser && loggedUser.id) {

            return await apiCall.get<DeviceDTO[]>(
                `${AppConstants.DEVICE_URL}${AppConstants.DEVICE}/of-user/?ownerId=${loggedUser.id}`,
                {}
            ).then((resp) => resp.data as DeviceDTO[]);
        } else {
            console.error("No logged user found or user ID is missing.");
            return null;
        }
    } catch (error: any) {
        console.error("Error fetching devices for user:", error.message || error);
        return null;
    }
};

