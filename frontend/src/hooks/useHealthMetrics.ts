import { useQuery } from "@tanstack/react-query";
import api from "../services/api";
import type { ApiResponse, HealthMetric } from "../types";

export function useHealthMetrics(apiId: string) {
  return useQuery({
    queryKey: ["health-metrics", apiId],
    queryFn: async () => {
      const { data } = await api.get<ApiResponse<HealthMetric[]>>(
        `/health-metrics/${apiId}`
      );
      return data.data;
    },
    refetchInterval: 30000, // 30-second polling fallback
    enabled: !!apiId,
  });
}
