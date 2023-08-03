//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2023.08.03 at 09:37:47 AM IDT 
//


package com.predict.data.jaxb;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice maxOccurs="2">
 *         &lt;element ref="{}PRD-by-ticks"/>
 *         &lt;element ref="{}PRD-by-second"/>
 *       &lt;/choice>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "prdByTicksOrPRDBySecond"
})
@XmlRootElement(name = "PRD-termination")
public class PRDTermination {

    @XmlElements({
        @XmlElement(name = "PRD-by-ticks", type = PRDByTicks.class),
        @XmlElement(name = "PRD-by-second", type = PRDBySecond.class)
    })
    protected List<Object> prdByTicksOrPRDBySecond;

    /**
     * Gets the value of the prdByTicksOrPRDBySecond property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the prdByTicksOrPRDBySecond property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPRDByTicksOrPRDBySecond().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link PRDByTicks }
     * {@link PRDBySecond }
     * 
     * 
     */
    public List<Object> getPRDByTicksOrPRDBySecond() {
        if (prdByTicksOrPRDBySecond == null) {
            prdByTicksOrPRDBySecond = new ArrayList<Object>();
        }
        return this.prdByTicksOrPRDBySecond;
    }

}
