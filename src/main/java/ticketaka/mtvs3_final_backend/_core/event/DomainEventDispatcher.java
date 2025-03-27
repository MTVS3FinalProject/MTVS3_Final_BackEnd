package ticketaka.mtvs3_final_backend._core.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class DomainEventDispatcher {

    private final ApplicationContext applicationContext;

    public void dispatchAll(List<Object> events) {
        for (Object event : events) {
            DomainEventHandler<Object> handler = findHandler(event);
            if (handler != null) {
                handler.handle(event);
            } else {
                log.warn("No handler found for event {}", event.getClass().getSimpleName());
            }
        }
    }

    @SuppressWarnings("unchecked")
    private DomainEventHandler<Object> findHandler(Object event) {
        Map<String, ?> handlers = applicationContext.getBeansOfType(DomainEventHandler.class);
        for (Object rawHandler : handlers.values()) {
            DomainEventHandler<?> handler = (DomainEventHandler<?>) rawHandler;
            if (supports(handler, event)) {
                return (DomainEventHandler<Object>) handler;
            }
        }

        return null;
    }

    private boolean supports(DomainEventHandler<?> handler, Object event) {
        return Arrays.stream(handler.getClass().getGenericInterfaces())
                .filter(type -> type instanceof ParameterizedType)
                .map(type -> (ParameterizedType) type)
                .map(ParameterizedType::getActualTypeArguments)
                .anyMatch(args -> Arrays.stream(args)
                        .anyMatch(arg -> arg.getTypeName().equals(event.getClass().getName())));
    }
}
