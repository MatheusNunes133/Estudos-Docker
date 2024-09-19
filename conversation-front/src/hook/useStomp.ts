// hooks/useStomp.js
import { useEffect, useState } from "react";
import Stomp from "stompjs";
import SockJS from "sockjs-client";
import { format } from "date-fns";

interface IPropsSendGlobalMesssage {
  url: string;
  message: string;
  sender: string;
}

interface IPropsSendPrivateMessage {
  sender: string;
  recipient: string;
  message: string;
  url: string;
}

const useWebSocket = (
  urlConection: string,
  chanel: string,
  privateChanel: string
) => {
  const [webSocket, setWebSocket] = useState<Stomp.Client | null>(null);
  const [globalMessages, setGlobalMessages] = useState<string[]>([]);
  const [privateMessages, setPrivateMessages] = useState<IPrivateMsgPayload[]>(
    []
  );

  useEffect(() => {
    if (!webSocket) {
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
        stompClient.subscribe(privateChanel, (privateMessage) => {
          const recivedPrivateMessages = JSON.parse(privateMessage.body);
          console.log(recivedPrivateMessages);
          setPrivateMessages((msg) => [...msg, recivedPrivateMessages]);
        });
      });

      setWebSocket(stompClient);

      return () => {
        stompClient.disconnect(() => {
          console.log("WebSocket Disconectado!");
        });
      };
    }
  }, [urlConection, chanel, privateChanel]);

  function sendGlobalMessage({
    message,
    sender,
    url,
  }: IPropsSendGlobalMesssage) {
    if (webSocket?.connected) {
      const bodyMessage = {
        sender,
        message,
      };
      webSocket.send(url, {}, JSON.stringify(bodyMessage));
    }
  }

  function sendPrivateMessage({
    message,
    recipient,
    sender,
    url,
  }: IPropsSendPrivateMessage) {
    if (webSocket?.connected) {
      const data = {
        message,
        recipient,
        sender,
      };
      webSocket.send(url, {}, JSON.stringify(data));

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
