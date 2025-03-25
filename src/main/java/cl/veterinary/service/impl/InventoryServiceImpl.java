package cl.veterinary.service.impl;

import cl.veterinary.model.Inventory;
import cl.veterinary.repository.InventoryRepository;
import cl.veterinary.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class InventoryServiceImpl implements InventoryService {

   @Autowired
   InventoryRepository inventoryRepository;

    @Override
    public List<Inventory> findAll() {
        return inventoryRepository.findAll();
    }

    @Override
    public Optional<Inventory> findById(Long id) {
        return inventoryRepository.findById(id);
    }

    @Override
    public Inventory save(Inventory inventory) {
        return inventoryRepository.save(inventory);
    }


    @Override
    public void delete(Long id) {
       inventoryRepository.deleteById(id);
    }

    @Override
    public List<Inventory> loadfromCSV(InputStream inputStream)throws IOException {
        List<Inventory> lista = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(";");
                Inventory dto = new Inventory();
                dto.setNombre(datos[0]);
                dto.setTipo(datos[1]);
                dto.setDescripcion(datos[2]);
                dto.setStock(Integer.parseInt(datos[3]));
                dto.setUnidadMedida(datos[4]);
                dto.setFechaVencimiento(String.valueOf(LocalDate.parse(datos[5])));
                lista.add(save(dto));
            }
        }
        return lista;
    }
    @Override
    public List<Inventory> findStockCritico(int minimo) {
        return inventoryRepository.findAll().stream()
                .filter(i -> i.getStock() <= minimo)
                .collect(Collectors.toList());
    }
}
