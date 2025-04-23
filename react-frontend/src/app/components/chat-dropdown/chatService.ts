import {AppConstants} from "@/app/helper/AppConstants";
import {apiCall} from "@/app/api/ApiCall";
import {getLoggedUser} from "@/app/helper/PermissionHelper";
import {SendMessageDTO, TypingDTO} from "@/app/components/chat-dropdown/chatDTO";

export const fetchConversations = async (userId: string) => {
    try {
        const response = await apiCall.get(AppConstants.CHAT_URL + `/conversation/conversations?userId=${userId}`);
        return response.data;
    } catch (error) {
        console.error('Error fetching conversations:', error);
        throw error;
    }
};

export const fetchMessages = async (conversationId: string) => {
    try {
        const response = await apiCall.get(AppConstants.CHAT_URL + `/conversation/${conversationId}/messages`);
        return response.data;
    } catch (error) {
        console.error('Error fetching messages:', error);
        throw error;
    }
};

export const sendWsMessage = async (conversationId: string, content: string, sendMessage: (destination: string, message: any) => void) => {
    const loggedUser = await getLoggedUser();
    if (!loggedUser?.id) {
        throw new Error('User is not logged in.');
    }
    if (loggedUser.id) {
        const sendMessageDTO: SendMessageDTO = {
            senderId: loggedUser.id,
            conversationId: conversationId,
            content: content,
        };
        sendMessage("/app/send-message", sendMessageDTO)

    }
};

export const markRead = async (conversationId: string, userId: string, sendMessage: (destination: string, message: any) => void) => {
    const loggedUser = await getLoggedUser();
    if (!loggedUser?.id) {
        throw new Error('User is not logged in.');
    }
    if (loggedUser.id) {
        const markReadDTO = {
            senderId: userId,
            conversationId: conversationId,
        };
        sendMessage("/app/mark-read", markReadDTO);
    }
};

export const sendTypingNotification = async (conversationId: string, receiverId: string, sendMessage: (destination: string, message: any) => void) => {
    const loggedUser = await getLoggedUser();
    if (!loggedUser?.id) {
        throw new Error('User is not logged in.');
    }
    if (loggedUser.id) {
        const typingDTO: TypingDTO = {
            conversationId: conversationId,
            receiverId: receiverId,
        };

        sendMessage("/app/typing", typingDTO);
    }
};
