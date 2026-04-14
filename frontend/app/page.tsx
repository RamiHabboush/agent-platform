"use client";

import { useState } from "react";

export default function Home() {
  const [input, setInput] = useState("");
  const [messages, setMessages] = useState<string[]>([]);
  const [loading, setLoading] = useState(false);

  const send = async () => {
    if (!input.trim()) return;

    // show user message
    setMessages((prev) => [...prev, `${input}`]);
    setLoading(true);

    try {
      const res = await fetch("http://localhost:8080/agent", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ input }),
      });

      const text = await res.text();

      setMessages((prev) => [...prev, `${text}`]);
      setInput("");
    } catch {
      setMessages((prev) => [...prev, "Server error"]);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="p-4 max-w-xl mx-auto">
      <h1 className="text-xl font-bold mb-4">AI Agent</h1>

      <div className="border p-3 h-80 overflow-y-auto mb-3">
        {messages.map((m, i) => (
          <p key={i} className="mb-2">{m}</p>
        ))}
        {loading && <p>⏳ Thinking...</p>}
      </div>

      <div className="flex gap-2">
        <input
          className="border p-2 flex-1"
          value={input}
          onChange={(e) => setInput(e.target.value)}
          onKeyDown={(e) => e.key === "Enter" && send()}
        />

        <button
          onClick={send}
          className="bg-blue-500 text-white px-4"
        >
          Send
        </button>
      </div>
    </div>
  );
}