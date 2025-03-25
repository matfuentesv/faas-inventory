package cl.veterinary.service;

import cl.veterinary.model.Inventory;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

public interface InventoryService {

    Inventory save(Inventory inventory);
    List<Inventory> findAll();
    Optional<Inventory> findById(Long id);
    void delete(Long id);
    List<Inventory> loadfromCSV(InputStream inputStream)throws IOException;
    List<Inventory> findStockCritico(int minimo);

}
