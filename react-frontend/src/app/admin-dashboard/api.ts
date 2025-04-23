import {DeviceDTO} from "@/app/models/DeviceDTO";
import {AppConstants} from "@/app/helper/AppConstants";
import {UserDTO} from "@/app/models/UserDTO";
import {NewUserDTO} from "@/app/models/NewUserDTO";
import {NewDeviceDTO} from "@/app/models/NewDeviceDTO";
import {apiCall} from "@/app/api/ApiCall";


export const getDevices = () => {
    return apiCall.get<DeviceDTO[]>(AppConstants.DEVICE_URL + AppConstants.DEVICE, {});
}

export const getUsers = () => {
    return apiCall.get<UserDTO[]>(AppConstants.USER_URL + AppConstants.USER, {})
}

export const addUser = (newUser: NewUserDTO) => {
    return apiCall.post(AppConstants.USER_URL + AppConstants.USER, newUser)
}

export const updateUser = (user: UserDTO) => {
    return apiCall.put(AppConstants.USER_URL + AppConstants.USER, user)
}

export const addDevice = (device: NewDeviceDTO) => {
    return apiCall.post(AppConstants.DEVICE_URL + AppConstants.DEVICE, device)
}

export const updateDevice = (device: DeviceDTO) => {
    return apiCall.put(AppConstants.DEVICE_URL + AppConstants.DEVICE, device)
}