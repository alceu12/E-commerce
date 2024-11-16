package com.Ecommerce.Ecommerce.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Ecommerce.Ecommerce.entity.Cupom;
import com.Ecommerce.Ecommerce.repository.CupomRepository;

@Service
public class CupomService {

    @Autowired
    private CupomRepository cupomRepository;

    public Cupom criarCupom(Cupom cupom) {
        return cupomRepository.save(cupom);
    }

    public Optional<Cupom> obterCupomPorId(Long id) {
        return cupomRepository.findById(id);
    }

    public List<Cupom> listarCupons() {
        return cupomRepository.findAll();
    }

    public Optional<Cupom> validarCupom(String codigo) {
        LocalDate hoje = LocalDate.now();
        return cupomRepository.findByCodigoAndAtivoIsTrue(codigo)
                .filter(cupom -> !hoje.isBefore(cupom.getDataInicio()) && !hoje.isAfter(cupom.getDataFim()));
    }

    public Cupom atualizarCupom(Long id, Cupom cupomAtualizado) {
        Cupom cupomExistente = cupomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cupom não encontrado"));

        cupomExistente.setCodigo(cupomAtualizado.getCodigo());
        cupomExistente.setValorDesconto(cupomAtualizado.getValorDesconto());
        cupomExistente.setValorMinimo(cupomAtualizado.getValorMinimo());
        cupomExistente.setDataInicio(cupomAtualizado.getDataInicio());
        cupomExistente.setDataFim(cupomAtualizado.getDataFim());
        cupomExistente.setAtivo(cupomAtualizado.isAtivo());

        return cupomRepository.save(cupomExistente);
    }

    public void deletarCupom(Long id) {
        cupomRepository.deleteById(id);
    }
}
