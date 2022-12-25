package jpabook.jpashop.repository;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepository {
    private final EntityManager em;

    public void save(Item item){
        if(item.getId()==null)em.persist(item);
        else em.merge(item);//영속성 컨텍스트에 이미 있는 경우
    }

    public Item findOne(Long id){
        return em.find(Item.class, id);
    }
    public List<Item> findAll(){
        return em.createQuery("select m from Item m", Item.class)
                .getResultList();
    }

    public List<Item> findAllByName(String name){
        return em.createQuery("select m from Item m where m.name=:name", Item.class)
                .setParameter("name", name)
                .getResultList();
    }

}
