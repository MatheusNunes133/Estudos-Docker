interface IPayload {
  roles: string[];
  nickname: string;
  userId: string;
  username: string;
  sub: string;
}

interface IPrivateMsgPayload {
  sender: string;
  senderEmail?: string;
  recipient: string;
  message: string;
  dateTime: string;
}

interface IGlobalMsgPayload {
  sender: string;
  message: string;
  dateTime: string;
}
