'use client';

import {useRouter} from 'next/navigation';
import './styles.css';
import {useState} from "react";
import ChatDropdown from "@/app/components/chat-dropdown/ChatDropdown";
import ChatIcon from '@mui/icons-material/Chat';
import LogoutIcon from '@mui/icons-material/Logout';
import {apiCall} from "@/app/api/ApiCall";
import {AppConstants} from "@/app/helper/AppConstants";

interface NavbarParams {
    notLogin: boolean
}

const Navbar = ({notLogin}: NavbarParams) => {
    const router = useRouter();
    const [isChatOpen, setIsChatOpen] = useState(false);


    const handleLogout = () => {
        logout();
    };

    const logout = () => {
        apiCall.post(AppConstants.USER_URL + '/login/logout', {})
            .then(() => {
                router.push('/');
            })
            .catch((error) => {
                console.error("Error during logout:", error);
                alert("Failed to log out. Please try again.");
            });
    };


    const toggleChatDropdown = () => {
        setIsChatOpen((prev) => !prev);
    };

    return (
        <nav>
            <div>
                <h1>Energy Management System</h1>
            </div>
            <div style={{display: 'flex', width: '10%'}}>
                {
                    notLogin &&

                    <ChatIcon onClick={toggleChatDropdown} style={{marginRight: '30%'}}/>

                }
                {isChatOpen && <ChatDropdown isOpen={isChatOpen} toggleDropdown={toggleChatDropdown}/>}

                {
                    notLogin && <LogoutIcon onClick={handleLogout}/>
                }
            </div>
        </nav>
    )
        ;
};

export default Navbar