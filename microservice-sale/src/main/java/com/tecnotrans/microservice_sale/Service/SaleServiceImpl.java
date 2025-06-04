package com.tecnotrans.microservice_sale.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

///import com.microservice.sale.client.PerfumeClient; Esto aún no existe en nuestro proyecto
///mport com.microservice.course.dto.StudentDTO;
///import com.microservice.course.http.response.StudentByCourseResponse;
import com.tecnotrans.microservice_sale.Model.Sale;
import com.tecnotrans.microservice_sale.Repository.ISaleRepository;

import java.util.List;

@Service
public class SaleServiceImpl implements ISaleService{

    @Autowired
    private ISaleRepository iSaleRepository;

    /*@Autowired
    private StudentClient studentClient; no tenemos un equivalente a esto. Hay que hacer un clinete y utilizar OpenFeing*/

    @Override
    public List<Sale> findAll() {
        return (List<Sale>) iSaleRepository.findAll();
    }

    @Override
    public Sale findById(Long id) {
        return iSaleRepository.findById(id).orElseThrow();
    }

    @Override
    public void save(Sale sale) {
        iSaleRepository.save(sale);
    }

    @Override
    public void deleteById(Long id){
        iSaleRepository.deleteById(id);
    }

    /*@Override
    public StudentByCourseResponse findStudentsByIdCourse(Long idCourse){


        //Consultar el curso
        //Porque devuelve un optional
        Course course = iCourseRepository.findById(idCourse).orElse(new Course());

        //Obtener los estudiantes que estan en el curso obtenido
        List<StudentDTO> studentDTOList = studentClient.findAllStudentByCourse(idCourse);


        return StudentByCourseResponse.builder()
                .courseName(course.getName())
                .teacher(course.getTeacher())
                .studentDTOList(studentDTOList)
                .build();
    }*/ 
    
    ///Aun no tenemos un equivalente para esto

}
