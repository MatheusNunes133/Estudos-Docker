"use client";

import React, { useState, useEffect } from "react";
import List from "@mui/material/List";
import ListItem from "@mui/material/ListItem";
import { ListItemAvatar, Avatar, Button, TextField } from "@material-ui/core";
import useWebSocket from "@/hook/useStomp";
import { getCookie } from "cookies-next";
import { decodeToken } from "react-jwt";
import { format } from "date-fns";
import { toastError } from "@/services/toast.service";

export default function Chat() {
  const [userId, setUserId] = useState<string>("");
  const [sub, setSub] = useState<string>("");
  const [nickname, setNickname] = useState<string>("");
  const token = getCookie("token") as string;
  useEffect(() => {
    (async () => {
      if (token) {
        const decodedToken: IPayload = (await decodeToken(token)) as any;
        if (decodedToken) {
          setUserId(decodedToken.userId);
          setSub(decodedToken.sub);
          setNickname(decodedToken.nickname);
        }
      }
    })();
  }, []);

  const webSocket = useWebSocket(
    "http://localhost:8080/conversation/connect",
    "/global/messages"
  );

  const [globalMessage, setGlobalMessage] = useState("");
  const [privateMessage, setPrivateMessage] = useState("");
  const [recipient, setRecipient] = useState("");

  function submitGlobalMessage() {
    if (globalMessage !== "" && globalMessage !== null) {
      webSocket.sendGlobalMessage({
        message: globalMessage,
        sender: userId,
        url: "/app/chat",
      });

      setGlobalMessage("");
    }
  }

  function submitPrivateMessage() {
    try {
      if (
        privateMessage !== "" &&
        privateMessage !== null &&
        recipient !== null &&
        recipient !== ""
      ) {
        if (recipient !== sub) {
          webSocket.sendPrivateMessage({
            message: privateMessage,
            sender: sub,
            recipient,
            url: `/app/privateChat`,
            token: token,
          });

          setPrivateMessage("");
        } else {
          toastError("Você não pode enviar mensagem para você mesmo!");
        }
      } else {
        toastError("Preencha os campos!");
      }
    } catch (error) {
      console.log(error);
      toastError("Email de usuário não encontrado!");
    }
  }

  return (
    <>
      <div>
        <List>
          <h1>Chat Global</h1>
          {webSocket.globalMessages.map((msg: any, index) => (
            <ListItem key={index}>
              {msg.sender === nickname
                ? `${msg.dateTime.replace("T", " às ")} @Eu: ${msg.message}`
                : `${msg.dateTime.replace("T", " às ")} @${msg.sender}: ${
                    msg.message
                  }`}
            </ListItem>
          ))}
        </List>
        <div style={{ display: "flex" }}>
          <TextField
            label="Chat Global"
            variant="outlined"
            value={globalMessage}
            onChange={(e) => setGlobalMessage(e.target.value)}
          />
          <Button onClick={submitGlobalMessage}>Send</Button>
        </div>
      </div>
      <div>
        <List>
          <h1>Chat Privado</h1>
          {webSocket.privateMessages.map((msg: IPrivateMsgPayload, index) => (
            <>
              <ListItem
                key={index}
                onClick={() =>
                  setRecipient(msg.senderEmail ? msg.senderEmail : "")
                }
                style={{ cursor: "pointer" }}
              >
                {msg.sender === sub
                  ? `${msg.dateTime} @Eu: ${msg.message}`
                  : `${msg.dateTime.replace("T", " às ")} @${msg.sender}: ${
                      msg.message
                    }`}
              </ListItem>
            </>
          ))}
        </List>
        <div style={{ display: "flex", flexDirection: "column" }}>
          <TextField
            label="Email do destinatário"
            variant="outlined"
            value={recipient}
            onChange={(e) => setRecipient(e.target.value)}
          />
          <TextField
            label="Chat privado"
            variant="outlined"
            value={privateMessage}
            onChange={(e) => setPrivateMessage(e.target.value)}
          />
          <Button onClick={submitPrivateMessage}>Enviar</Button>
        </div>
      </div>
    </>
  );
}
