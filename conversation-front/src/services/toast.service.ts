import { toast } from "react-toastify";

export function toastSucess(message: string) {
  return toast.success(message);
}

export function toastError(message: string) {
  return toast.error(message);
}

export function toastInfo(message: string) {
  return toast.info(message);
}

export function toastWarn(message: string) {
  return toast.warn(message);
}
