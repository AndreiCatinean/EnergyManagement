import {AppConstants} from "@/app/helper/AppConstants";
import {apiCall} from "@/app/api/ApiCall";

export const deleteUser = (userId: string) => {

    return apiCall.delete(AppConstants.USER_URL + AppConstants.USER + "/?id=" + userId)
}