package com.predict.data.source;

import com.predict.data.jaxb.*;
import com.predict.engine.def.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class File {
    private final static String JAXB_XML_GAME_PACKAGE_NAME = "com.predict.data.jaxb";

    public static World fetchDataFromFile() {
        try {
            InputStream inputStream = new FileInputStream(new java.io.File("com/predict/resources/ex1-cigarets.xml"));
            PRDWorld world = deserializeFrom(inputStream);

            Environment environment = buildEnvironment(world.getPRDEvironment());
            Map<String, Entity> entities = buildEntities(world.getPRDEntities());

            List<Rule> rules = buildRules(world.getPRDRules());

            Termination termination = buildTermination(world.getPRDTermination());

            return new World(environment, entities, rules, termination);
            // TODO: create the special functions

        } catch (JAXBException | FileNotFoundException e) {
            e.printStackTrace();
        }
        return new World();
    }

    private static Environment buildEnvironment(PRDEvironment fileEnv) {
        Environment environment = new Environment();
        for(PRDEnvProperty fileEnvProp : fileEnv.getPRDEnvProperty()) {
            Property p = buildEnvPropertyByType(fileEnvProp);
            System.out.println(p.toString());
            environment.addProperty(p);
        }

        return environment;
    }

    private static Map<String, Entity> buildEntities(PRDEntities fileEntities) {
        Map<String, Entity> entities = new HashMap<>();
        for(PRDEntity fileEntity : fileEntities.getPRDEntity()) {
            Entity e = buildEntity(fileEntity);
            System.out.println(e.toString());
            entities.put(e.getName(), e);
        }
        return entities;
    }

    private static Termination buildTermination(PRDTermination fileTermination) {
        PRDByTicks fileTicks = null;
        PRDBySecond fileSeconds = null;
        for(Object fileTerm : fileTermination.getPRDByTicksOrPRDBySecond()) {
            if(fileTerm.getClass().toString().equals("class com.predict.data.jaxb.PRDByTicks"))
                fileTicks = (PRDByTicks)(fileTerm);
            if(fileTerm.getClass().toString().equals("class com.predict.data.jaxb.PRDBySecond"))
                fileSeconds = (PRDBySecond)(fileTerm);
        }
        int ticks = (fileTicks != null ? fileTicks.getCount() : -1);
        int seconds = (fileSeconds != null ? fileSeconds.getCount() : -1);
        return new Termination(ticks, seconds);
    }

    private static List<Rule> buildRules(PRDRules fileRules) {
        List<Rule> rules = new ArrayList<>();
        for(PRDRule fileRule : fileRules.getPRDRule()) {
            Rule r = buildRule(fileRule);
            System.out.println(r);
            rules.add(r);
        }
        return rules;
    }

    private static Rule buildRule(PRDRule fileRule) {
        List<Action> actions = buildActions(fileRule.getPRDActions());
        int ticks = 1;
        double probability = 1;
        if(fileRule.getPRDActivation() != null) {
            if(fileRule.getPRDActivation().getTicks() != null) ticks = fileRule.getPRDActivation().getTicks();
            if(fileRule.getPRDActivation().getProbability() != null) probability = fileRule.getPRDActivation().getProbability();
        }
        Rule r = new Rule(fileRule.getName(), ticks, probability, actions);
        return r;
    }

    private static List<Action> buildActions(PRDActions fileActions) {
        List<Action> actions = new ArrayList<>();
        for(PRDAction fileAction : fileActions.getPRDAction()) {
            Action a = buildAction(fileAction);
            actions.add(a);
        }
        return actions;
    }

    private static Action buildAction(PRDAction fileAction) {
        if(fileAction.getType().equals(ActionType.INCREASE) || fileAction.getType().equals(ActionType.DECREASE) ) {
            // TODO: validate the by attribute
            return new Action(fileAction.getType(), fileAction.getEntity(), fileAction.getProperty(), fileAction.getBy());
        }
        else if(fileAction.getType().equals(ActionType.CALCULATION)) {
            // TODO: return error when type is NO_TYPE or any needed attribute
            Calculation.TYPES type = Calculation.TYPES.NO_TYPE;
            String arg1 = "", arg2 = "";
            if(fileAction.getPRDMultiply() != null) {
                type = Calculation.TYPES.MULT;
                arg1 = fileAction.getPRDMultiply().getArg1();
                arg2 = fileAction.getPRDMultiply().getArg2();
            }
            else if(fileAction.getPRDDivide() != null) {
                type = Calculation.TYPES.DIV;
                arg1 = fileAction.getPRDDivide().getArg1();
                arg2 = fileAction.getPRDDivide().getArg2();
            }
            return new Calculation(fileAction.getEntity(), fileAction.getResultProp(), type, arg1, arg2);
        }
        else if(fileAction.getType().equals(ActionType.CONDITION)) {
            return buildCondition(fileAction.getPRDCondition(), fileAction.getEntity(), fileAction.getProperty(), fileAction.getPRDThen(), fileAction.getPRDElse(), Condition.TYPE.OUTER);
        }
        else if(fileAction.getType().equals(ActionType.SET)) {
            return new Action(fileAction.getType(), fileAction.getEntity(), fileAction.getProperty(), fileAction.getValue());
        }
        else if(fileAction.getType().equals(ActionType.KILL)) {
            return new Action(fileAction.getType(), fileAction.getEntity());
        }
        return new Action();
    }

    private static Condition buildCondition(PRDCondition fileCondition, String entity, String property, PRDThen fileThen, PRDElse fileElse, Condition.TYPE t) {
        Action thenAction = null, elseAction = null;
        if(t == Condition.TYPE.OUTER) {
            thenAction = buildAction(fileThen.getPRDAction().get(0));
            if(fileElse != null) elseAction = buildAction(fileElse.getPRDAction().get(0));
        }

        if(fileCondition.getSingularity().equals(Condition.SINGLE)) {
            return new SingleCondition(fileCondition.getEntity(), fileCondition.getProperty(), thenAction, elseAction, fileCondition.getOperator(), fileCondition.getValue());
        }
        else if(fileCondition.getSingularity().equals(Condition.MULTIPLE)) {
            List<Condition> conditions = new ArrayList<>();
            for(PRDCondition c1 : fileCondition.getPRDCondition()) {
                Condition c = buildCondition(c1, entity, property, fileThen, fileElse, Condition.TYPE.INNER);
                conditions.add(c);
            }
            return new MultipleCondition(entity, property, thenAction, elseAction, conditions, fileCondition.getLogical());
        }
        return new Condition();
    }

    private static Entity buildEntity(PRDEntity fileEntity) {
        Entity entity = new Entity(fileEntity.getName(), fileEntity.getPRDPopulation());
        for(PRDProperty fileProp : fileEntity.getPRDProperties().getPRDProperty()) {
            Property p = buildPropertyByType(fileProp);
            entity.addProperty(p);
        }

        return entity;
    }

    private static Property buildEnvPropertyByType(PRDEnvProperty fileEnvProp) {
        Property p;
        if(fileEnvProp.getType().equals(PropertyType.DECIMAL))
            p = new Property<Integer>(fileEnvProp.getPRDName());
        if(fileEnvProp.getType().equals(PropertyType.FLOAT))
            p = new Property<Float>(fileEnvProp.getPRDName());
        if(fileEnvProp.getType().equals(PropertyType.BOOLEAN))
            p = new Property<Boolean>(fileEnvProp.getPRDName());
        if(fileEnvProp.getType().equals(PropertyType.STRING))
            p = new Property<String>(fileEnvProp.getPRDName());
        else
            p = new Property<Integer>(fileEnvProp.getPRDName());

        if(fileEnvProp.getPRDRange() != null) {
            p.setRange(new Range(fileEnvProp.getPRDRange().getFrom(), fileEnvProp.getPRDRange().getTo()));
        }

        return p;
    }

    private static Property buildPropertyByType(PRDProperty fileProp) {
        Property p;
        if(fileProp.getType().equals(PropertyType.DECIMAL))
            p = new Property<Integer>(fileProp.getPRDName());
        if(fileProp.getType().equals(PropertyType.FLOAT))
            p = new Property<Float>(fileProp.getPRDName());
        if(fileProp.getType().equals(PropertyType.BOOLEAN))
            p = new Property<Boolean>(fileProp.getPRDName());
        if(fileProp.getType().equals(PropertyType.STRING))
            p = new Property<String>(fileProp.getPRDName());
        else
            p = new Property<Integer>(fileProp.getPRDName());

        if(fileProp.getPRDRange() != null) {
            p.setRange(new Range(fileProp.getPRDRange().getFrom(), fileProp.getPRDRange().getTo()));
        }

        if(fileProp.getPRDValue().isRandomInitialize()) {
            p.setRandom(true);
        }
        else if(fileProp.getPRDValue().getInit() != null && fileProp.getPRDValue().getInit().length() > 0) {
            // TODO: check if init value fits the type
            p.setInit(fileProp.getPRDValue().getInit());
        }

        return p;
    }

    private static PRDWorld deserializeFrom(InputStream in) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(JAXB_XML_GAME_PACKAGE_NAME);
        Unmarshaller u = jc.createUnmarshaller();
        return (PRDWorld) u.unmarshal(in);
    }
}
