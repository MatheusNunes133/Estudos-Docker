// hooks/useStomp.js
import { useEffect, useState } from "react";
import Stomp from "stompjs";
import SockJS from "sockjs-client";
import { format } from "date-fns";
import { getCookie } from "cookies-next";
import { decodeToken } from "react-jwt";
import { toastError } from "@/services/toast.service";
import { Api } from "@/services/axios.service";

interface IPropsSendGlobalMesssage {
  url: string;
  message: string;
  sender: string;
}

interface IPropsSendPrivateMessage {
  sender: any;
  recipient: string;
  message: string;
  url: string;
  token?: string;
}

const useWebSocket = (urlConection: string, chanel: string) => {
  const [webSocket, setWebSocket] = useState<Stomp.Client | null>(null);
  const [globalMessages, setGlobalMessages] = useState<IGlobalMsgPayload[]>([]);
  const [privateMessages, setPrivateMessages] = useState<IPrivateMsgPayload[]>(
    []
  );

  useEffect(() => {
    if (!webSocket) {
      const token = getCookie("token") as string;
      const decodedToken: IPayload = decodeToken(token) as any;

      const socket = new SockJS(urlConection);
      const stompClient = Stomp.over(socket);
      //Faz a conexão com o Canal e busca mensagens
      stompClient.connect({}, (frame) => {
        console.log("Conectado:" + frame);

        //Faz a inscrição no canal global pra receber todas as mensagens globais
        stompClient.subscribe(chanel, (message) => {
          console.log(message);
          const recivedMessages = JSON.parse(message.body);
          console.log(recivedMessages);
          setGlobalMessages((msg) => [...msg, recivedMessages]);
        });

        //Faz a inscrição no canal privado pra receber todas as mensagens privadas
        stompClient.subscribe(
          `/private/${decodedToken.userId}/restrict/messages`,
          (privateMessage) => {
            const recivedPrivateMessages = JSON.parse(privateMessage.body);
            console.log(privateMessage);
            setPrivateMessages((msg) => [...msg, recivedPrivateMessages]);
          }
        );
      });

      setWebSocket(stompClient);

      return () => {
        if (stompClient.connected) {
          stompClient.disconnect(() => {
            console.log("WebSocket Disconectado!");
          });
        }
      };
    }
  }, [urlConection, chanel]);

  async function sendGlobalMessage({
    message,
    sender,
    url,
  }: IPropsSendGlobalMesssage) {
    if (webSocket?.connected) {
      const bodyMessage = {
        sender,
        message,
      };
      await webSocket.send(url, {}, JSON.stringify(bodyMessage));
    }
  }

  async function sendPrivateMessage({
    message,
    recipient,
    sender,
    url,
    token,
  }: IPropsSendPrivateMessage) {
    if (webSocket?.connected) {
      const data = {
        message,
        recipient,
        sender,
      };
      try {
        const recipientExists = await Api.get(`/findRecipient/${recipient}`, {
          headers: {
            Authorization: `Bearer ${token}`,
            "Access-Control-Allow-Origin": "*",
          },
        });

        if (recipientExists.data.recipientExists === false) {
          toastError("Usuário inexistente!");
          return;
        }
        await webSocket.send(url, {}, JSON.stringify(data));
        // Adiciona a mensagem enviada ao estado local imediatamente
        setPrivateMessages((prevMessages) => [
          ...prevMessages,
          {
            sender,
            recipient,
            message,
            dateTime: format(new Date(), "dd-MM-yyyy 'às' HH:mm"),
          },
        ]);
      } catch (error) {
        toastError("Erro ao enviar mensagem!");
      }
    }
  }

  return {
    globalMessages,
    privateMessages,
    sendGlobalMessage,
    sendPrivateMessage,
  };
};

export default useWebSocket;
