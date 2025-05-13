package tn.esprit.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tn.esprit.Entities.Supplier;
import tn.esprit.Services.SupplierService;

import java.util.List;

@RestController
@RequestMapping("/suppliers")
public class SupplierController {

    @Autowired
    private SupplierService supplierService;

    @PostMapping
    public Supplier addSupplier(@RequestBody Supplier supplier) {
        return supplierService.addSupplier(supplier);
    }

    @PutMapping
    public Supplier updateSupplier(@RequestBody Supplier supplier) {
        return supplierService.updateSupplier(supplier);
    }

    @DeleteMapping("/{id}")
    public void deleteSupplier(@PathVariable Long id) {
        supplierService.deleteSupplier(id);
    }

    @GetMapping("/{id}")
    public Supplier getSupplierById(@PathVariable Long id) {
        return supplierService.getSupplierById(id);
    }

    @GetMapping
    public List<Supplier> getAllSuppliers() {
        return supplierService.getAllSuppliers();
    }
}
