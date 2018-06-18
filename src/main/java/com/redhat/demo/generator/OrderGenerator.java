package com.redhat.demo.generator;

import java.io.StringWriter;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

public class OrderGenerator {
	private static enum Destinations {
		AR("Buenos Aires"), AU("Canberra"), AT("Vienna"), BE("Brussels"), BR("Brasilia"), CA("Ottowa"), CL(
				"Santiago"), CN("Beijing"), CO("Bogota"), CZ("Prague"), DK("Copenhagen"), FI(
						"Helsinki"), FR("Paris"), DE("Berlin"), IT("Rome"), JP("Tokyo"), MX("Mexico City"), NL(
								"Amsterdam"), NO("Oslo"), PL("Warsaw"), RU("Moscow"), ZA("Pretoria"), KR("Seoul"), ES(
										"Madrid"), SE("Stockholm"), CH("Bern"), UK("London"), US("Washington, DC");

		private String city;

		Destinations(String city) {
			this.city = city;
		}

		public String getCity() {
			return city;
		}
	}

	private static final List<Destinations> DEST_VALUES = Collections
			.unmodifiableList(Arrays.asList(Destinations.values()));
	private static final int DEST_SIZE = DEST_VALUES.size();

	// Based on https://nationalzoo.si.edu/animals/list, basically every animal at
	// the
	// Washington DC National Zoo
	private static final String[] ANIMALS = { "Abyssinian ground hornbill", "African clawed frog", "Aldabra tortoise",
			"Allen's swamp monkey", "Alligator Snapping Turtle", "Alpaca", "American alligator", "American bison",
			"Andean bear", "Arapaima", "Asian elephant", "Asian small-clawed otter", "Asian water dragon",
			"Australian snake-necked turtle", "Bald eagle", "Banded leporinus", "Banded rock rattlesnake",
			"Barred tiger salamander", "Beaver", "Bennett's wallaby", "Black howler monkey", "Black pacu",
			"Black-and-white ruffed lemur", "Black-footed ferret", "Black-tailed prairie dog", "Bobcat",
			"Brazilian rainbow boa", "Brown pelican", "Brush-tailed bettong", "Burmese rock python", "Caiman lizard",
			"California sea lion", "Channel catfish", "Cheetah", "Chicken", "Chinese alligator",
			"Chinese crocodile lizard", "Clouded leopard", "Common Opossum", "Common raven", "Corn snake",
			"Coronated tree frog", "Cow", "Cuban crocodile", "Dama gazelle", "Damaraland mole rat", "Degu",
			"Dwarf mongoose", "Eastern box turtle", "Eastern diamondback rattlesnake", "Eastern indigo snake",
			"Eastern red-backed salamander", "Eld's deer", "Electric eel", "Emerald tree monitor", "Emperor newt",
			"Eyelash palm pitviper", "False water cobra", "Fennec fox", "Fiji banded iguana", "Fishing cat",
			"Flagtail characin", "Fly River turtle", "Fowler's toad", "Freshwater stingray", "Gaboon viper",
			"Geoffroy's marmoset", "Gharial", "Giant leaf-tailed gecko", "Giant panda", "Gila monster", "Goat",
			"Goeldi's monkey", "Golden lion tamarin", "Golden mantella frog", "Golden-headed lion tamarin",
			"Goliath bird-eating tarantula", "Grand Cayman blue iguana", "Gray seal", "Gray wolf", "Green anaconda",
			"Green aracari", "Green crested basilisk", "Green salamander", "Green tree python", "Grevy's zebra",
			"Guam kingfisher", "Guam rail", "Harbor seal", "Hellbender", "Hooded crane", "Japanese giant salamander",
			"Japanese koi", "King cobra", "Komodo dragon", "La Plata three-banded armadillo", "Lance head rattlesnake",
			"Land hermit crab", "Larger Malay mouse-deer", "Lemur tree frog", "Lesser Madagascar hedgehog tenrec",
			"Lesser kudu", "Lion", "Loggerhead shrike ", "Long-tailed chinchilla", "Long-tailed salamander",
			"Maned wolf", "Mangrove snake", "Matamata turtle", "Meerkat", "Meller's chameleon", "Miniature donkey",
			"Naked mole-rat", "New Caledonian gecko", "North American porcupine", "North American river otter",
			"North Island brown kiwi", "Northern copperhead", "Northern pine snake", "Northern red salamander",
			"Northern tree shrew", "Norway rat", "Orangutan", "Oriental fire-bellied toad", "Ossabaw Island hog",
			"Painted river terrapin", "Pale-headed saki monkey", "Panamanian golden frog", "Patagonian Mara",
			"Persian Onager", "Philippine crocodile", "Plecostomus", "Poison frogs", "Prehensile-tailed porcupine",
			"Prevost's squirrel", "Przewalski's horse", "Radiated tortoise", "Red River hog", "Red panda", "Red siskin",
			"Red-bellied piranha", "Red-crested cardinal", "Red-crowned crane", "Red-footed tortoise",
			"Red-fronted lemur", "Red-ruffed lemur", "Red-rumped agouti", "Ring-tailed lemur", "Rock hyrax",
			"Roseate spoonbill", "Ruppell's griffon vulture", "SCBI Animals", "Sand cat", "Schmidt's red-tailed monkey",
			"Scimitar-horned oryx", "Screaming hairy armadillo", "Siamang", "Silver arowana", "Sinaloan milksnake",
			"Sitatunga", "Sloth bear", "Smooth-sided toad", "Southern tamandua", "Southern two-toed sloth",
			"Spider tortoise", "Striped skunk", "Sunbittern", "Tanagers", "Tentacled Snake", "Tiger", "Timor python",
			"Titi monkey", "Tokay gecko", "Tomato frog", "Twig catfish", "Vietnamese mossy frog",
			"Von der Decken's hornbill", "Western lowland gorilla", "White's tree frog", "White-cheeked gibbon",
			"White-naped crane", "White-nosed coati", "Yellow-spotted Amazon river turtle" };

	private static final int BASE_ID = 4000;
	private static final int MAX_ORDERLINES = 4;
	private static final int MAX_ANIMAL_QUANTITY = 50;

	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
	private static final Random RAND = new SecureRandom();

	public String toXml() throws Exception {
		Order order = generate();

		JAXBContext jaxbContext = JAXBContext.newInstance(Order.class);
		Marshaller marshaller = jaxbContext.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

		StringWriter writer = new StringWriter();
		marshaller.marshal(order, writer);

		return writer.toString();
	}

	private Order generate() {
		String date = DATE_FORMAT.format(Calendar.getInstance().getTime());
		String year = date.substring(0, 4);

		Order order = new Order();

		String randomId = String.format("%04d", BASE_ID + RAND.nextInt(10000 - BASE_ID));
		order.setId(year + "_" + randomId);

		Customer customer = new Customer();
		customer.setId("B" + randomId);

		Destinations dest = DEST_VALUES.get(RAND.nextInt(DEST_SIZE));
		customer.setName(dest.getCity() + " Zoo");
		customer.setCity(dest.getCity());
		customer.setCountry(dest.name());

		order.setCustomer(customer);
		order.setDate(date);

		int numOrderlines = RAND.nextInt(MAX_ORDERLINES) + 1;
		List<OrderLine> lines = new ArrayList<OrderLine>(numOrderlines);

		for (int i = 0; i < numOrderlines; i++) {
			Article article = new Article();

			int animalIdx = RAND.nextInt(ANIMALS.length);
			String id = String.format("Z%04d", animalIdx);
			article.setId(id);
			article.setDescription(ANIMALS[animalIdx]);

			OrderLine orderLine = new OrderLine();
			orderLine.setArticle(article);
			orderLine.setQuantity(RAND.nextInt(MAX_ANIMAL_QUANTITY));

			lines.add(orderLine);
		}

		OrderLines orderlines = new OrderLines();
		orderlines.setOrderLines(lines);

		order.setOrderlines(orderlines);

		return order;
	}
}