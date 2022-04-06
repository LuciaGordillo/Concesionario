package com.concesionario.app.repository.specifications;
import com.concesionario.app.domain.Coche;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;


public interface CocheSpecification  {
   public static Specification<Coche> busquedaCoche(String filtro)

 {
        final Logger log = LoggerFactory.getLogger(CocheSpecification.class);
        return new Specification<Coche>(){
            private static final long serialVersionUID = 1L;

            @Override
            public Predicate toPredicate(Root<Coche> coche, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
   
            
           
            List<Predicate> organizador = new ArrayList<>();
                log.debug("LLega a specification");
            Expression<String> colorCoche = coche.get("color").as(String.class);
            if(null != filtro)  {
                
                    

                    log.debug("LLega a busquedaistado");

                    organizador.add(criteriaBuilder.like(colorCoche,"%"+filtro+"%"));
                    
                
                }
                return criteriaBuilder.and(organizador.toArray(new Predicate[] {})); 
            
           
           
            }

        };
 


}
}
