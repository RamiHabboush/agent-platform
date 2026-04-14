"use client";

import { useState } from "react";

export default function Home() {
  const [input, setInput] = useState("");
  const [messages, setMessages] = useState<string[]>([]);

  const send = async () => {
    try {
      const res = await fetch("http://localhost:8080/agent", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ input }),
      });

      const text = await res.text();

      setMessages((prev) => [...prev, text]);
      setInput("");
    } catch {
      setMessages((prev) => [...prev, "Server error"]);
    }
  };

  return (
    <div className="p-4">
      <h1>AI Agent</h1>

      <input
        className="border p-2"
        value={input}
        onChange={(e) => setInput(e.target.value)}
      />

      <button onClick={send}>Send</button>

      <div>
        {messages.map((m, i) => (
          <p key={i}>{m}</p>
        ))}
      </div>
    </div>
  );
}