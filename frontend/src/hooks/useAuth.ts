import { useQuery } from "@tanstack/react-query";
import api from "../services/api";

interface User {
  username: string;
  displayName: string;
  role: "viewer" | "operator" | "admin";
}

export function useAuth() {
  const { data: user, isLoading } = useQuery({
    queryKey: ["auth", "me"],
    queryFn: async () => {
      const { data } = await api.get<{ data: User }>("/auth/me");
      return data.data;
    },
    retry: false,
  });

  return {
    user,
    isLoading,
    isAuthenticated: !!user,
    isAdmin: user?.role === "admin",
    isOperator: user?.role === "operator" || user?.role === "admin",
  };
}
