'use client';

import React, {useEffect, useRef, useState} from 'react';
import {useWebSocket} from "@/app/components/chat-web-socket/WebScoketProvider";
import {
    fetchConversations,
    fetchMessages,
    markRead,
    sendTypingNotification,
    sendWsMessage
} from "@/app/components/chat-dropdown/chatService";
import './styles.css';
import {ConversationDTO, MessageDTO, ParticipantDTO, TypingDTO} from "@/app/components/chat-dropdown/chatDTO";
import {getLoggedUser} from "@/app/helper/PermissionHelper";
import {getUsers} from "@/app/admin-dashboard/api";
import {UserDTO} from "@/app/models/UserDTO";
import {AppConstants} from "@/app/helper/AppConstants";
import AddIcon from '@mui/icons-material/Add';
import CheckIcon from '@mui/icons-material/Check';
import DoneAllIcon from '@mui/icons-material/DoneAll';
import NotificationImportantIcon from '@mui/icons-material/NotificationImportant';
import {apiCall} from "@/app/api/ApiCall";

interface ChatDropdownProps {
    isOpen: boolean;
    toggleDropdown: () => void;
}

const ChatDropdown: React.FC<ChatDropdownProps> = ({isOpen, toggleDropdown}) => {
    const [conversations, setConversations] = useState<ConversationDTO[]>([]);
    const [selectedConversation, setSelectedConversation] = useState<ConversationDTO | null>(null);
    const [messagesMap, setMessagesMap] = useState<Map<string, MessageDTO[]>>(new Map());
    const [messageContent, setMessageContent] = useState<string>('');
    const [users, setUsers] = useState<UserDTO[]>([]);
    const [isUserListOpen, setIsUserListOpen] = useState(false);
    const [loggedUser, setLoggedUser] = useState<UserDTO>({id: "", isAdmin: false, username: ""})
    const messagesContainerRef = useRef<HTMLDivElement>({} as HTMLDivElement);
    const [typingMap, setTypingMap] = useState<Map<string, boolean>>(new Map());


    const {subscribeToGroup, subscribeToUser, subscribeToTyping, sendMessage} = useWebSocket();


    useEffect(() => {
        getLoggedUser().then((respLoggedUser) => {
            if (respLoggedUser) {
                setLoggedUser(respLoggedUser);
            }
        }).catch((error) => {
            console.error("Error getting logged user", error);
        });
    }, []);


    useEffect(() => {
        if (loggedUser.id) {
            fetchConversations(loggedUser.id).then((conversations) => {
                setConversations(conversations);

                conversations.forEach((conversation: ConversationDTO) => {
                    fetchMessages(conversation.id).then((messages) => {
                        setMessagesMap((prevMap) => {
                            const updatedMap = new Map(prevMap);
                            updatedMap.set(conversation.id, messages);
                            return updatedMap;
                        });
                    });
                });
            });

            getUsers().then((response) => {
                setUsers(response.data);
            });
        }
    }, [loggedUser]);

    useEffect(() => {
        if (loggedUser.id) {
            subscribeToGroup((message: string) => {
                handleIncomingMessage(message);
            });

            subscribeToUser(loggedUser.id, (message: string) => {
                handleIncomingMessage(message);
            });

            subscribeToTyping(loggedUser.id, (message: string) => {
                handleTyping(message);
            });
        }
    }, [subscribeToGroup, subscribeToUser, loggedUser]);

    const handleIncomingMessage = (message: string) => {
        try {
            const parsedMessage: MessageDTO = JSON.parse(message);
            const {conversationId} = parsedMessage;

            setMessagesMap((prevMap) => {
                const updatedMap = new Map(prevMap);
                if (!updatedMap.has(conversationId)) {
                    fetchConversations(loggedUser.id).then((conversations) => {
                        setConversations((prevConversations) => {
                            return [
                                ...prevConversations,
                                ...conversations.filter(
                                    (conversation: ConversationDTO) =>
                                        !prevConversations.some(
                                            (existingConversation) =>
                                                existingConversation.id === conversation.id
                                        )
                                ),
                            ];
                        });

                        updatedMap.set(conversationId, [parsedMessage]);
                        setMessagesMap(new Map(updatedMap));
                    });

                } else {
                    const existingMessages = updatedMap.get(conversationId) || [];

                    const existingMessageIndex = existingMessages.findIndex((msg) => msg.id === parsedMessage.id);

                    if (existingMessageIndex === -1) {
                        updatedMap.set(conversationId, [...existingMessages, parsedMessage]);
                    } else {
                        const updatedMessages = [...existingMessages];
                        updatedMessages[existingMessageIndex] = {
                            ...existingMessages[existingMessageIndex],
                            read: parsedMessage.read,
                        };
                        updatedMap.set(conversationId, updatedMessages);
                        setMessagesMap(new Map(updatedMap));
                    }
                }
                return updatedMap;
            });
        } catch (error) {
            console.error("Error processing incoming message:", error);
        }
    };

    const handleCreateChat = (selectedUserId: string) => {
        apiCall
            .post(AppConstants.CHAT_URL + `/conversation/one-to-one`, null, {
                params: {
                    user1Id: loggedUser.id,
                    user2Id: selectedUserId,
                },
            })
            .then((response) => {
                const newConversation: ConversationDTO = response.data;

                setConversations((prevConversations) => [...prevConversations, newConversation]);

                setMessagesMap((prevMap) => {
                    const updatedMap = new Map(prevMap);
                    updatedMap.set(newConversation.id, []);
                    return updatedMap;
                });

                setIsUserListOpen(false);
                handleSelectConversation(newConversation);
            })
            .catch((error) => {
                console.error("Error creating chat:", error);
            });
    };
    const handleSelectConversation = (conversation: ConversationDTO) => {
        setSelectedConversation(conversation);
    };
    const handleTyping = (message: string) => {
        const parsedMessage: TypingDTO = JSON.parse(message);
        updateTypingStatus(parsedMessage);
    };

    const updateTypingStatus = (parsedMessage: TypingDTO) => {
        const {conversationId} = parsedMessage;

        setTypingMap((prevMap) => {
            const updatedMap = new Map(prevMap);
            updatedMap.set(conversationId, true);
            return updatedMap;
        });

        setTimeout(() => {
            setTypingMap((prevMap) => {
                const updatedMap = new Map(prevMap);
                updatedMap.set(conversationId, false);
                return updatedMap;
            });
        }, 3000);
    };

    useEffect(() => {

        if (selectedConversation) {
            markMessagesAsRead(selectedConversation.id);
            if (messagesContainerRef.current) {
                messagesContainerRef.current.scrollTop = messagesContainerRef.current.scrollHeight;
            }
        }
    }, [selectedConversation, messagesMap, typingMap]);

    const markMessagesAsRead = (conversationId: string) => {
        const participants = selectedConversation?.participants || [];

        const otherParticipants = participants.filter((participant) => participant.uuid !== loggedUser.id);

        otherParticipants.forEach((participant) => {
            markRead(conversationId, participant.uuid, sendMessage).then(() => {
            })
        });
    };

    const handleSendMessage = () => {
        if (messageContent && selectedConversation) {
            sendWsMessage(selectedConversation.id, messageContent, sendMessage).then(() => {
                setMessageContent('');
            });
        }
    };


    const handleTypingNotification = (conversationId: string, receiverId: string) => {
        sendTypingNotification(conversationId, receiverId, sendMessage).then(() => {
        })
    };

    useEffect(() => {
        if (messageContent !== "") {
            if (selectedConversation) {
                selectedConversation.participants.forEach((participant) => {
                    if (participant.uuid !== loggedUser.id) {
                        handleTypingNotification(selectedConversation.id, participant.uuid);
                    }
                });
            }
        }
    }, [messageContent]);

    const getFilteredUsers = () => {
        const conversationUserIds = new Set(
            conversations.flatMap((conversation) =>
                conversation.participants.map((participant) => participant.uuid)
            )
        );
        conversationUserIds.add(loggedUser.id);

        return users.filter((user) => !conversationUserIds.has(user.id));
    };

    if (!isOpen) return null;

    return (
        <div className="chat-dropdown">
            <button onClick={toggleDropdown} className="close-btn">X</button>

            <div className="header">
                <h4>Conversations</h4>
                <button
                    className="add-chat-btn"
                    onClick={() => setIsUserListOpen((prev) => !prev)}
                >
                    <AddIcon/>
                </button>
            </div>

            {isUserListOpen && (
                <div className="user-list">
                    {getFilteredUsers().map((user) => (
                        <div
                            key={user.id}
                            className="user-item"
                            onClick={() => handleCreateChat(user.id)}
                        >
                            {user.username}
                        </div>
                    ))}
                </div>
            )}

            <div className="conversations-list">
                {conversations.map((conversation) => {
                    const unreadMessages = messagesMap.get(conversation.id)?.some(
                        (msg) => msg.senderId !== loggedUser.id && !msg.read
                    );

                    return (
                        <div
                            key={conversation.id}
                            className="conversation-item"
                            onClick={() => handleSelectConversation(conversation)}
                        >
                            {conversation.participants.length === 0 ? (
                                <span>Group Chat</span>
                            ) : (
                                <span>
                        Conversation with:{' '}
                                    {conversation.participants
                                        .filter((participant: ParticipantDTO) => participant.uuid !== loggedUser.id)
                                        .map((participant: ParticipantDTO) => participant.username)
                                        .join(', ')}
                    </span>
                            )}
                            {unreadMessages && conversation.participants.length !== 0 && (
                                <NotificationImportantIcon
                                    style={{
                                        color: 'red',
                                        marginLeft: '10px',
                                    }}
                                />
                            )}
                        </div>
                    );
                })}
            </div>

            {selectedConversation && (
                <>
                    <h4>Messages</h4>
                    <div className="messages-container" ref={messagesContainerRef}>
                        {messagesMap.get(selectedConversation.id)?.map((msg, index) => (
                            <div
                                key={index}
                                className={`message ${selectedConversation.participants.length === 0
                                    ? 'group-chat-message' 
                                    : msg.senderId === loggedUser.id
                                        ? 'my-message'
                                        : 'other-message'}`}
                            >
                                {msg.content}
                                {msg.senderId === loggedUser.id && !(selectedConversation.participants.length === 0) && !msg.read && (
                                    <CheckIcon style={{fontSize: '16px', marginLeft: '5%'}}/>
                                )}
                                {msg.senderId === loggedUser.id && !(selectedConversation.participants.length === 0) && msg.read && (
                                    <DoneAllIcon style={{fontSize: '16px', color: 'blue', marginLeft: '5%'}}/>
                                )}
                            </div>

                        ))}
                        {typingMap.get(selectedConversation.id) && (
                            <div className="typing-notification">
                                <span className="typing-dots">...</span> is typing
                            </div>
                        )}
                    </div>

                    {(selectedConversation.participants.length !== 0 || loggedUser.isAdmin) &&
                        <div className="send-message">
                            <input
                                type="text"
                                value={messageContent}
                                onChange={(e) => setMessageContent(e.target.value)}
                                placeholder="Type a message"
                            />
                            <button onClick={handleSendMessage}>Send</button>
                        </div>}
                </>
            )}
        </div>
    );
};

export default ChatDropdown;
