import { NextRequest, NextResponse } from "next/server";

export function middleware(req: NextRequest, resp: NextResponse) {
  try {
    const foundToken = req.cookies.get("token");

    if (!foundToken) {
      return NextResponse.redirect(new URL("/", req.url));
    }

    return NextResponse.next();
  } catch (error) {
    req.cookies.clear();
    return NextResponse.redirect(new URL("/", req.url));
  }
}

export const config = {
  matcher: ["/chat"],
};
