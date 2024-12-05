"use client";

import React, { useState, useEffect } from "react";
import List from "@mui/material/List";
import ListItem from "@mui/material/ListItem";
import { ListItemAvatar, Avatar, Button, TextField } from "@material-ui/core";
import useWebSocket from "@/hook/useStomp";
import { getCookie } from "cookies-next";
import { decodeToken } from "react-jwt";
import { format } from "date-fns";

export default function Chat() {
  const token = getCookie("token") as string;

  const [userId, setUserId] = useState("");
  const [sub, setSub] = useState("");

  useEffect(() => {
    if (token) {
      const decodedToken: IPayload = decodeToken(token) as any;
      if (decodedToken) {
        setUserId(decodedToken.userId);
        setSub(decodedToken.sub);
      }
    }
  }, [token]);

  const webSocket = useWebSocket(
    "http://localhost:8080/conversation/connect",
    "/global/messages",
    `/private/${sub}/restrict/messages`
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
        recipient !== null
      ) {
        webSocket.sendPrivateMessage({
          message: privateMessage,
          sender: sub,
          recipient,
          url: `/app/privateChat`,
        });

        setPrivateMessage("");
      }
    } catch (error) {
      console.log(error);
      alert("Email de usuário não encontrado!");
    }
  }

  return (
    <>
      <div>
        <List>
          <h1>Chat Global</h1>
          {webSocket.globalMessages.map((msg: any, index) => (
            <ListItem key={index}>
              {`${format(new Date(), "dd-MM-yyyy 'às' HH:mm")} @${msg.user}: ${
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
