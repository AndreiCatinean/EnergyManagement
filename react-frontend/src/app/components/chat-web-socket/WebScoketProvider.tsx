'use client';

import React, {createContext, PropsWithChildren, useContext, useEffect, useState} from "react";
import {Client} from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import {AppConstants} from "@/app/helper/AppConstants";

interface WebSocketContextType {
    subscribeToGroup: (callback: (message: any) => void) => void;
    subscribeToUser: (userId: string, callback: (message: any) => void) => void;
    subscribeToTyping: (userId: string, callback: (message: any) => void) => void;
    sendMessage: (destination: string, message: any) => void;
}

const WebSocketContext = createContext<WebSocketContextType | undefined>(undefined);


export const WebSocketProvider: React.FC<PropsWithChildren<{}>> = ({children}) => {
    const [stompClient, setStompClient] = useState<Client | null>(null);
    const [subscriptions, setSubscriptions] = useState<Set<string>>(new Set());

    useEffect(() => {
        const socket = new SockJS(AppConstants.CHAT_URL + "/ws");
        const client = new Client({
            webSocketFactory: () => socket,
            onConnect: () => {
            },
            onDisconnect: () => {
            },
            onStompError: () => {
            }
        });

        client.activate();

        setStompClient(client);

        return () => {
            client.deactivate().then(() => {
            }).catch((error) => {
                console.error('Error during STOMP disconnection:', error);
            });
        };
    }, []);

    const subscribeToGroup = (callback: (message: any) => void) => {
        const subscriptionKey = "/topic/group";

        if (stompClient && !subscriptions.has(subscriptionKey)) {
            stompClient.subscribe(subscriptionKey, (message) => {
                callback(message.body);
            });
            setSubscriptions(prevSubscriptions => new Set(prevSubscriptions).add(subscriptionKey));
        }
    };

    const subscribeToUser = (userId: string, callback: (message: any) => void) => {
        const subscriptionKey = `/queue/${userId}`;

        if (stompClient && !subscriptions.has(subscriptionKey)) {
            stompClient.subscribe(subscriptionKey, (message) => {
                callback(message.body);
            });
            setSubscriptions(prevSubscriptions => new Set(prevSubscriptions).add(subscriptionKey));
        }
    };

    const subscribeToTyping = (userId: string, callback: (message: any) => void) => {
        const subscriptionKey = `/typing/${userId}`;

        if (stompClient && !subscriptions.has(subscriptionKey)) {
            stompClient.subscribe(subscriptionKey, (message) => {
                callback(message.body);
            });

            setSubscriptions(prevSubscriptions => new Set(prevSubscriptions).add(subscriptionKey));
        }
    };

    const sendMessage = (destination: string, message: any) => {
        if (stompClient && stompClient.connected) {
            console.log(destination)
            console.log(message)

            stompClient.publish({
                destination: destination,
                body: JSON.stringify(message),
            });
        } else {
            console.error('WebSocket is not connected.');
        }
    };

    return (
        <WebSocketContext.Provider value={{subscribeToGroup, subscribeToUser, subscribeToTyping, sendMessage}}>
            {children}
        </WebSocketContext.Provider>
    );
};

export const useWebSocket = (): WebSocketContextType => {
    const context = useContext(WebSocketContext);
    if (!context) {
        throw new Error("useWebSocket must be used within a WebSocketProvider");
    }
    return context;
};
