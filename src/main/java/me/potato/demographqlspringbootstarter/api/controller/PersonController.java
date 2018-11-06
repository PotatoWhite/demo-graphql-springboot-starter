package me.potato.demographqlspringbootstarter.api.controller;

import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.schema.DataFetcher;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import me.potato.demographqlspringbootstarter.api.dao.PersonRepository;
import me.potato.demographqlspringbootstarter.api.entity.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.net.ssl.HttpsURLConnection;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

@RestController
public class PersonController {

	@Autowired
	PersonRepository personRepository;


	private GraphQL graphQL;

	@PostConstruct
	public void loadSchema() throws IOException {
		TypeDefinitionRegistry registry = new SchemaParser().parse(new ClassPathResource("person.graphql").getFile());
		RuntimeWiring wiring = buildWiring();

		GraphQLSchema schema = new SchemaGenerator().makeExecutableSchema(registry, wiring);
		graphQL = GraphQL.newGraphQL(schema).build();
	}

	private RuntimeWiring buildWiring() {
		DataFetcher<List<Person>> fetcher01 = data -> {
			return (List<Person>) personRepository.findAll();
		};

		DataFetcher<Person> fetcher02 = data -> {
			return personRepository.findByEmail(data.getArgument("email"));
		};

		return RuntimeWiring.newRuntimeWiring().type("Query",
				typeWriting -> typeWriting.dataFetcher("getAllPerson", fetcher01).dataFetcher("findPerson", fetcher02))
				.build();

	}

	@PostMapping("/addPerson")
	public String addPerson(@RequestBody List<Person> persons) {
		personRepository.saveAll(persons);
		return "add persons size = " + persons.size();
	}

	@GetMapping("/findAllPerson")
	public List<Person> getPersons() {
		return (List<Person>) personRepository.findAll();
	}

	@PostMapping("/graphqls")
	public ResponseEntity<Object> getAll(@RequestBody String query){
		ExecutionResult result = graphQL.execute(query);
		return new ResponseEntity<Object>(result, HttpStatus.OK);
	}


}
