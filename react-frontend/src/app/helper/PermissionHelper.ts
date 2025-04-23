import {AppRouterInstance} from "next/dist/shared/lib/app-router-context.shared-runtime";
import {UserDTO} from "@/app/models/UserDTO";
import {apiCall} from "@/app/api/ApiCall";
import {AppConstants} from "@/app/helper/AppConstants";

export const getLoggedUser = (): Promise<UserDTO | null> => {
    return new Promise((resolve) => {
        if (typeof window !== 'undefined') {
            apiCall.get(AppConstants.USER_URL + '/login/me', {})
                .then((response) => {
                    resolve(response.data as UserDTO);
                })
                .catch((error) => {
                    console.error('Error fetching logged user:', error);
                    resolve(null);
                });
        } else {
            resolve(null);
        }
    });
};

export const redirectBasedOnUser = async (router: AppRouterInstance) => {
    const loggedUser = await getLoggedUser();

    if (loggedUser !== null) {
        if (loggedUser.isAdmin) {
            router.push('/admin-dashboard');
        } else {
            router.push('/personal-devices');
        }
    } else {
        router.push('/');
    }
};


export const determineAuthorization = async (router: AppRouterInstance, pathname: string) => {
    const loggedUser = await getLoggedUser();

    if (!loggedUser) {
        router.push('/');
        return false;
    } else if (loggedUser.isAdmin && pathname === '/personal-devices') {
        router.push('/admin-dashboard')
        return false;
    } else if (!loggedUser.isAdmin && pathname === '/admin-dashboard') {
        router.push('/personal-devices')
        return false;
    }
    return true;
};

export const isUserAdmin = async () => {
    const loggedUser = await getLoggedUser();


    return loggedUser?.isAdmin
};

export const detLoggedUserDeleted = async (userId: string, router: AppRouterInstance) => {
    const loggedUser = await getLoggedUser();

    if (loggedUser?.id === userId) {
        sessionStorage.removeItem('loggedUser')
        router.push('/')
    }
}