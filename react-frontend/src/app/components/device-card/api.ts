import {AppConstants} from "@/app/helper/AppConstants";
import {apiCall} from "@/app/api/ApiCall";

export const deleteDevice = (deviceId: string) => {

    return apiCall.delete(AppConstants.DEVICE_URL + AppConstants.DEVICE + "/?deviceId=" + deviceId)
}