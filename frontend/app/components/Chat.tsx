const sendMessage = (message: string) => {
  const eventSource = new EventSource(
    `http://localhost:8080/chat/stream?input=${encodeURIComponent(message)}`
  );

  let fullResponse = "";

  eventSource.onmessage = (event) => {
    fullResponse += event.data;

    setMessages((prev) => [
      ...prev.slice(0, -1),
      { role: "assistant", content: fullResponse },
    ]);
  };

  eventSource.onerror = () => {
    eventSource.close();
  };
};