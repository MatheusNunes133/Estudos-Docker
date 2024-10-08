import type { Metadata } from "next";

export const metadata: Metadata = {
  title: "Login para chat",
  description: "Generated by create next app",
  robots: {
    index: true,
    noimageindex: true,
  },
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="pt-BR">
      <head>
        <meta name="theme-color" content="#68E1FD" />
      </head>
      <body>
        {children}
      </body>
    </html>
  );
}
