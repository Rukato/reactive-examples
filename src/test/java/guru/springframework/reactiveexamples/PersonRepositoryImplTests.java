package guru.springframework.reactiveexamples;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import guru.springframework.reactiveexamples.domain.Person;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class PersonRepositoryImplTests {

    PersonRepositoryImpl personRepositoryImpl;


    @BeforeEach
    void setUp() {
        personRepositoryImpl = new PersonRepositoryImpl();
    }

    @Test
    void getByIdBlock() {
        Mono<Person> personMono = personRepositoryImpl.getById(1);

        Person person = personMono.block();

        System.out.println(person.toString());
    }

    @Test
    void getByIdSubcribe() {
        Mono<Person> personMono = personRepositoryImpl.getById(3);

        personMono.subscribe(person -> {
            System.out.println(person.toString());
        });
    }

    @Test
    void getByIdMapFunction() {
        Mono<Person> personMono = personRepositoryImpl.getById(1);

        personMono.map(person -> {
            System.out.println(person.toString());

            return person.getFirstName();
        }).subscribe(firstName -> {
            System.out.println("From map: " + firstName);
        });
    }

    @Test
    void fluxTestBlockFirst() {
        Flux<Person> personFlux = personRepositoryImpl.findAll();

        Person person = personFlux.blockFirst();

        System.out.println(person.toString());


    }

    @Test
    void testFluxSubscribe() {
        Flux<Person> personFlux = personRepositoryImpl.findAll();

        personFlux.subscribe(person -> {
            System.out.println(person.toString());
        });
    }

    @Test
    void testFluxToListMono() {
        Flux<Person> personFlux = personRepositoryImpl.findAll();

        Mono<List<Person>> personListMono = personFlux.collectList();
        
        personListMono.subscribe(list -> {
            list.forEach(person -> {
                System.out.println(person.toString());
            });
        });
    }

    @Test
    void testFindPersonById() {
        Flux<Person> personFlux = personRepositoryImpl.findAll();

        final Integer id = 3;

        Mono<Person> personMono = personFlux.filter(person -> person.getId() == id).next();

        personMono.subscribe(person -> {
            System.out.println(person.toString());
        });
    }

    @Test
    void testFindPersonByIdNotFoundWithException() {
        Flux<Person> personFlux = personRepositoryImpl.findAll();

        final Integer id = 8;

        Mono<Person> personMono = personFlux.filter(person -> person.getId() == id).single();

        personMono.doOnError(throwable -> {
            System.out.println("I go Boom!");
        }).onErrorReturn(
            Person.builder().id(8).build()
        )
        .subscribe(person -> {
            System.out.println(person.toString());
        });
    }
}
