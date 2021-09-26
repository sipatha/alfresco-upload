package za.sipatha.upload.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TrashcanService {

    private final DeletedService deletedService;

    private final DeleteService deleteService;

    public void empty() {
        deletedService.get()
            .forEach(entry -> {
                deleteService.delete(entry.getEntry().getId());
            });
    }

}
