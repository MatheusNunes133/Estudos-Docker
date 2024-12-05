"use client";

import { Api } from "@/services/axios.service";
import React, { useState } from "react";
import { setCookie } from "cookies-next";
import style from "./page.module.scss";
import {
  Button,
  CircularProgress,
  IconButton,
  InputAdornment,
  Link,
  TextField,
} from "@mui/material";
import { LoadingButton } from "@mui/lab";
import Visibility from "@mui/icons-material/Visibility";
import VisibilityOff from "@mui/icons-material/VisibilityOff";
import { isEmail } from "validator";
import { toastError } from "@/services/toast.service";
import AccountCircleIcon from "@mui/icons-material/AccountCircle";
import LoginIcon from "@mui/icons-material/Login";
import { useRouter } from "next/navigation";

export default function App() {
  const router = useRouter();
  const [isLoading, setIsLoading] = useState(false);
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [showPassword, setShowPassword] = useState(false);

  const [emailError, setEmailError] = useState(false);
  const [passwordError, setpasswordError] = useState(false);

  function handleShowPassword() {
    setShowPassword(!showPassword);
  }

  async function login() {
    setIsLoading(true);
    if (!email || !isEmail(email)) {
      setEmailError(true);
      setIsLoading(false);
      return;
    }
    if (!password) {
      setpasswordError(true);
      setIsLoading(false);
      return;
    }

    try {
      const bodyLogin = {
        email,
        password,
      };

      const token = (await Api.post("/login", bodyLogin)).data;
      setCookie("token", token.token);
      router.replace("/chat");
    } catch (error: any) {
      toastError(
        error.response === undefined
          ? "Erro ao efetuar login!"
          : error.response.data.error
      );
      setIsLoading(false);
    }
  }

  return (
    <main className={style.container}>
      <div className={style.login}>
        <AccountCircleIcon className={style.icon} />
        <div className={style.credentials}>
          <TextField
            error={emailError}
            helperText={emailError ? "Email Incorreto!" : ""}
            onFocus={() => {
              setEmailError(false);
            }}
            type="email"
            label="Email"
            variant="outlined"
            value={email}
            onChange={(event) => setEmail(event.target.value)}
          />
          <TextField
            error={passwordError}
            helperText={passwordError ? "Informe uma senha" : ""}
            onFocus={() => {
              setpasswordError(false);
            }}
            type={showPassword ? "text" : "password"}
            label="Senha"
            variant="outlined"
            value={password}
            onChange={(event) => setPassword(event.target.value)}
            slotProps={{
              input: {
                endAdornment: (
                  <InputAdornment position="end">
                    <IconButton onClick={handleShowPassword}>
                      {showPassword ? <VisibilityOff /> : <Visibility />}
                    </IconButton>
                  </InputAdornment>
                ),
              },
            }}
          />
        </div>
        <LoadingButton
          loading={isLoading}
          className={style.loginButton}
          onClick={login}
          variant="contained"
          loadingIndicator={
            <CircularProgress size={24} sx={{ color: "white" }} />
          }
        >
          {!isLoading && "Entrar"}
        </LoadingButton>
        <Button href="/cadastro" className={style.signupButton}>
          CADASTRAR-SE
        </Button>
      </div>
    </main>
  );
}
