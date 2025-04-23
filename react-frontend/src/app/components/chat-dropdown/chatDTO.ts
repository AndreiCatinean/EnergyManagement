export interface SendMessageDTO {
    senderId: string;
    conversationId: string;
    content: string;
}

export interface ConversationDTO {
    id: string;
    participants: ParticipantDTO[];
}

export interface TypingDTO{
    conversationId:string;
    receiverId: string;
}

export interface ParticipantDTO {
    uuid: string;
    username: string;
}

export interface MessageDTO {
    id:string;
    senderId: string;
    content: string;
    timestamp: string;
    conversationId: string;
    read: boolean;

}
