
import {LoginDTO} from "@/app/models/LoginDTO";

import {AppConstants} from "@/app/helper/AppConstants";
import {UserDTO} from "@/app/models/UserDTO";
import {apiCall} from "@/app/api/ApiCall";


export const loginUser = (loginDTO: LoginDTO) => {
    return apiCall.post<UserDTO>(AppConstants.USER_URL + AppConstants.LOGIN, loginDTO)
}
