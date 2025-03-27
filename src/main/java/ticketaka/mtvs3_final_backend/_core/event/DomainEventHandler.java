package ticketaka.mtvs3_final_backend._core.event;

public interface DomainEventHandler<T> {
    void handle(T event);
}
