package com.redhat.demo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

class CmdLineHandler {
	private Option pub = Option.builder("p").desc("Publish messages to a topic").hasArg(false).longOpt("pub")
			.required(false).build();
	private Option queue = Option.builder("q").desc("Send messages to a queue").hasArg(false).longOpt("queue")
			.required(false).build();
	private Option numMsgsOpt = Option.builder("n").desc("Number of messages").hasArg().longOpt("num-msgs").required()
			.build();
	private Options options = new Options();
	private CommandLine cmd;
	private int numMsgs;

	CmdLineHandler() {
		options.addOption(pub);
		options.addOption(queue);
		options.addOption(numMsgsOpt);
	}

	void parse(String[] args) throws IllegalArgumentException {
		try {
			CommandLineParser parser = new DefaultParser();
			cmd = parser.parse(options, args, true);
			numMsgs = Integer.parseInt(cmd.getOptionValue(numMsgsOpt.getOpt()));

			// set queue as default if neither pub nor queue provided
			if (!isPub() && !isQueue()) {
				List<String> newArgs = new ArrayList<String>(args.length + 1);
				newArgs.addAll(Arrays.asList(args));
				newArgs.add("-" + queue.getOpt());
				cmd = parser.parse(options, newArgs.toArray(args), true);
			}
		} catch (Exception e) {
			displayHelp();
			throw new IllegalArgumentException(e);
		}
	}

	boolean isPub() {
		return cmd.hasOption(pub.getOpt());
	}

	boolean isQueue() {
		return cmd.hasOption(queue.getOpt());
	}

	int getNumMsgs() {
		return numMsgs;
	}

	private void displayHelp() {
		String header = "Send and/or publish messages to a queue and/or topic, respectively.";
		String footer = "\nPlease report issues at https://github.com/rlucente-se-jboss/amq-driver";
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp("QueueDriver", header, options, footer, true);
	}
}