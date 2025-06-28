package org.ncapas.happypawsbackend.config;

import org.ncapas.happypawsbackend.Domain.Entities.*;
import org.ncapas.happypawsbackend.Domain.Enums.UserRol;
import org.ncapas.happypawsbackend.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.ncapas.happypawsbackend.Domain.Enums.PetSize;

@Component
public class DataSeeder implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private SizeRepository sizeRepository;

    @Autowired

    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ShelterRepository shelterRepository;

    @Autowired
    private SpeciesRepository speciesRepository;

    @Autowired
    private BreedRepository breedRepository;

    @Override
    public void run(String... args){
        seedRoles();
        seedSizes();
        seedDefaultUsers();
        seedShelters();
        seedSpecies();
        seedBreeds();
    }

    private void seedRoles(){
        for (UserRol userRol : UserRol.values()) {
            if (roleRepository.findRolByName(userRol).isEmpty()) {
                Rol rol = new Rol();
                rol.setName(userRol);
                roleRepository.save(rol);
            }
        }
    }


    private void seedSizes(){
        for (PetSize size : PetSize.values()) {
            createSizeIfNotExists(size);
        }
    }

    private void createSizeIfNotExists(PetSize name) {
        if (sizeRepository.findByName(name).isEmpty()) {
            Size size = new Size();
            size.setName(name);
            sizeRepository.save(size);
        }
    }


    private void seedDefaultUsers() {
        createUserIfNotExists("admin@happypaws.com", "Admin123!", "ADMIN", "12345678-1");
        createUserIfNotExists("colaborador@happypaws.com", "Colab456!", "COLABORADOR", "12345678-2");
        createUserIfNotExists("adoptante@happypaws.com", "Adop789!", "ADOPTANTE", "12345678-3");
    }

    private void createUserIfNotExists(String email, String password, String rolName, String dui) {
        if (userRepository.findUserByEmail(email).isEmpty() &&
                userRepository.findByDUI(dui).isEmpty()) {
            Rol rol = roleRepository.findRolByName(UserRol.valueOf(rolName))
                    .orElseThrow(() -> new RuntimeException("Rol no encontrado"));

            User user = new User();
            user.setName(rolName.toLowerCase());
            user.setEmail(email);
            user.setPassword(passwordEncoder.encode(password));
            user.setPhone("12345678");
            user.setDUI(dui);
            user.setRol(rol);
            user.setState(1);
            userRepository.save(user);
        }
    }

    private void seedShelters() {
        if (shelterRepository.count() == 0) {
            Shelter shelter = new Shelter();
            shelter.setName("Refugio Central");
            shelter.setAddress("San Salvador, San Salvador");
            shelter.setPhone(22223333);
            shelter.setEmail("contacto@refugiocentral.com");
            shelterRepository.save(shelter);
        }
    }

    private void seedSpecies() {
        if (speciesRepository.findAll().isEmpty()) {
            Species dog = new Species();
            dog.setName("Perro");
            speciesRepository.save(dog);

            Species cat = new Species();
            cat.setName("Gato");
            speciesRepository.save(cat);
        }
    }

    private void seedBreeds() {
        if (breedRepository.count() == 0) {
            Species dog = speciesRepository.findByName("Perro")
                    .orElseThrow(() -> new RuntimeException("Especie 'Perro' no encontrada"));
            Species cat = speciesRepository.findByName("Gato")
                    .orElseThrow(() -> new RuntimeException("Especie 'Gato' no encontrada"));

            createBreedIfNotExists("Labrador", dog);
            createBreedIfNotExists("Pastor Alemán", dog);
        }
    }

    private void createBreedIfNotExists(String name, Species species) {
        if (breedRepository.findByName(name).isEmpty()) {
            Breed breed = new Breed();
            breed.setName(name);
            breed.setSpecies(species);
            breedRepository.save(breed);
        }
    }
}
