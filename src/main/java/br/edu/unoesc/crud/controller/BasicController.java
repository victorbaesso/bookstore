package br.edu.unoesc.crud.controller;

import br.edu.unoesc.crud.model.Livro;
import br.edu.unoesc.crud.service.util.BasicCrud;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public abstract class BasicController<T> {

    @GetMapping({"/", "/index", "/lista", "lista"})
    public String index(@RequestParam("page") Optional<Integer> page, Model model) {
        int currentPage = page.orElse(1);
        Page<T> pageContent = getService().findPaginated(PageRequest.of(currentPage - 1, 12));

        model.addAttribute("page", pageContent);

        int totalPages = pageContent != null ? pageContent.getTotalPages() : 0;
        if (totalPages > 0) {
            Collection<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        imports(model);
        return getListPageName();
    }

    @GetMapping("/getList")
    public @ResponseBody
    Collection<T> getList() {
        Collection<T> objects = getService().listar();
        return !objects.isEmpty() ? objects : Arrays.asList(getService().getEmptyList());
    }

    protected String toIndex(String param, Model model) {
        model.addAttribute("msg", param);
        Page<T> pageContent = getService().findPaginated(PageRequest.of(0, 12));
        model.addAttribute("page", pageContent);
        return getListPageName();
    }

    protected void imports(Model model) {/** conteudo que precisa ser importado **/}

    protected abstract BasicCrud<T> getService();

    protected abstract String getListPageName();

}
