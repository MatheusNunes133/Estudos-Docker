"use client"

import { Api } from "@/services/axios.service"
import React, { useState } from "react"
import {setCookie} from "cookies-next"

export default function App(){

  const [email, setEmail] = useState("")
  const [password, setPassword] = useState("")

  async function login(){
    if(!email){
      alert("Email não informado!")
      return
    }
    if(!password){
      alert("Senha não informada!")
      return
    }

    try {
      const bodyLogin = {
        email,
        password
      }
  
      const token = (await Api.post("/login", bodyLogin)).data
      setCookie("token", token.token)
      window.location.replace("/chat")
    } catch (error:any) {
      alert(error.response.data.error)
    }

  }

  return(
    <>
    <div>
      <input type="text" placeholder="Email..." value={email} onChange={(event)=>setEmail(event.target.value)}/>
      <input type="password" placeholder="Password..." value={password} onChange={(event)=>setPassword(event.target.value)}/>
    </div>
    <button type="submit" onClick={login}>LOGIN</button>
    </>
  )
}