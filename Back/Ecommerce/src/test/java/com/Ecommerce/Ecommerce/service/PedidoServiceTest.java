package com.Ecommerce.Ecommerce.service;

import com.Ecommerce.Ecommerce.dto.*;
import com.Ecommerce.Ecommerce.entity.*;
import com.Ecommerce.Ecommerce.repository.*;
import com.Ecommerce.Ecommerce.util.PedidoMapper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import jakarta.persistence.EntityNotFoundException;

import java.time.LocalDate;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PedidoServiceTest {

    @Mock
    private PedidoRepository pedidoRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private CupomRepository cupomRepository;

    @Mock
    private ProdutoRepository produtoRepository;

    @Mock
    private ItemPedidoRepository itemPedidoRepository;

    @Mock
    private EnderecoRepository enderecoRepository;

    @InjectMocks
    private PedidoService pedidoService;

    private Usuario usuario;
    private Endereco enderecoUsuario;
    private Pedido pedido;
    private Cupom cupom;
    private ItemPedido itemPedido;
    private PedidoDTO pedidoDTO;
    private UsuarioDTO usuarioDTO;
    private ItemPedidoDTO itemPedidoDTO;
    private CupomDTO cupomDTO;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(10L);
        usuario.setNome("Usuario Teste");
        enderecoUsuario = new Endereco();
        enderecoUsuario.setRua("Rua Teste");
        enderecoUsuario.setNumero("123");
        enderecoUsuario.setCidade("Cidade Teste");
        enderecoUsuario.setEstado("Estado Teste");
        enderecoUsuario.setCep("00000-000");
        usuario.setEndereco(enderecoUsuario);

        itemPedido = new ItemPedido();
        itemPedido.setId(100L);
        itemPedido.setPrecoUnitario(50.0);
        itemPedido.setQuantidade(2);

        cupom = new Cupom();
        cupom.setId(5L);
        cupom.setValorMinimo(50.0);
        cupom.setValorDesconto(0.1); // 10% desconto

        usuarioDTO = new UsuarioDTO();
        usuarioDTO.setId(10L);

        itemPedidoDTO = new ItemPedidoDTO();
        itemPedidoDTO.setId(100L);

        pedidoDTO = new PedidoDTO();
        pedidoDTO.setUsuarioDTO(usuarioDTO);
        pedidoDTO.setItemPedidoDTO(Collections.singletonList(itemPedidoDTO));

        cupomDTO = new CupomDTO();
        cupomDTO.setId(5L);

        pedido = new Pedido();
        pedido.setId(1L);
        pedido.setUsuario(usuario);
        pedido.setStatusPedido(StatusPedido.PENDING_PAYMENT); // Definir status para evitar NPE
        pedido.setItemPedido(Collections.singletonList(itemPedido));
        pedido.setTotal(100.0);
        pedido.setDataPedido(LocalDate.now());
    }

    @Test
    void criarPedido_sucessoSemCupom() {
        when(usuarioRepository.findById(10L)).thenReturn(Optional.of(usuario));
        when(itemPedidoRepository.findById(100L)).thenReturn(Optional.of(itemPedido));
        when(enderecoRepository.save(any(Endereco.class))).thenAnswer(invocation -> {
            Endereco end = invocation.getArgument(0);
            return end; // Retorna o próprio endereço salvo
        });
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedido); // Retorna pedido com status definido

        PedidoDTO resultado = pedidoService.criarPedido(pedidoDTO);

        verify(usuarioRepository, times(1)).findById(10L);
        verify(itemPedidoRepository, times(1)).findById(100L);
        verify(pedidoRepository, times(1)).save(any(Pedido.class));
        Assertions.assertNotNull(resultado); // Agora não deve ser null
    }

    @Test
    void criarPedido_comCupomValido() {
        pedidoDTO.setCupomAplicado(cupomDTO);

        when(usuarioRepository.findById(10L)).thenReturn(Optional.of(usuario));
        when(itemPedidoRepository.findById(100L)).thenReturn(Optional.of(itemPedido));
        when(cupomRepository.findById(5L)).thenReturn(Optional.of(cupom));
        when(enderecoRepository.save(any(Endereco.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedido);

        PedidoDTO resultado = pedidoService.criarPedido(pedidoDTO);

        verify(cupomRepository, times(1)).findById(5L);
        Assertions.assertNotNull(resultado);
    }

    @Test
    void criarPedido_usuarioNaoEncontrado() {
        when(usuarioRepository.findById(10L)).thenReturn(Optional.empty());

        RuntimeException thrown = Assertions.assertThrows(RuntimeException.class, () -> {
            pedidoService.criarPedido(pedidoDTO);
        });

        Assertions.assertEquals("Usuário não encontrado.", thrown.getMessage());
        verify(pedidoRepository, never()).save(any());
    }

    @Test
    void criarPedido_itemPedidoNaoEncontrado() {
        when(usuarioRepository.findById(10L)).thenReturn(Optional.of(usuario));
        when(itemPedidoRepository.findById(100L)).thenReturn(Optional.empty());

        RuntimeException thrown = Assertions.assertThrows(RuntimeException.class, () -> {
            pedidoService.criarPedido(pedidoDTO);
        });

        Assertions.assertTrue(thrown.getMessage().contains("ItemPedido com ID 100 não encontrado."));
        verify(pedidoRepository, never()).save(any());
    }

    @Test
    void criarPedido_usuarioSemEndereco() {
        usuario.setEndereco(null); // usuário sem endereço
        when(usuarioRepository.findById(10L)).thenReturn(Optional.of(usuario));
        when(itemPedidoRepository.findById(100L)).thenReturn(Optional.of(itemPedido));

        RuntimeException thrown = Assertions.assertThrows(RuntimeException.class, () -> {
            pedidoService.criarPedido(pedidoDTO);
        });

        Assertions.assertEquals("Usuário não possui um endereço cadastrado.", thrown.getMessage());
        verify(pedidoRepository, never()).save(any());
    }

    @Test
    void obterTodosPedidos_sucesso() {
        Pedido outroPedido = new Pedido();
        outroPedido.setId(2L);
        outroPedido.setStatusPedido(StatusPedido.PENDING_PAYMENT); // Definir status
        when(pedidoRepository.findAll()).thenReturn(Arrays.asList(pedido, outroPedido));

        List<PedidoDTO> resultado = pedidoService.obterTodosPedidos();
        Assertions.assertEquals(2, resultado.size());
    }

    @Test
    void obterPedidoPorId_encontrado() {
        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));

        PedidoDTO resultado = pedidoService.obterPedidoPorId(1L);
        Assertions.assertNotNull(resultado);
    }

    @Test
    void obterPedidoPorId_naoEncontrado() {
        when(pedidoRepository.findById(1L)).thenReturn(Optional.empty());
        RuntimeException thrown = Assertions.assertThrows(RuntimeException.class, () -> {
            pedidoService.obterPedidoPorId(1L);
        });
        Assertions.assertEquals("Pedido não encontrado", thrown.getMessage());
    }

    @Test
    void atualizarPedido_sucesso() {
        // Necessário garantir que o pedido final terá status
        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));
        pedidoDTO.setTotal(200.0);
        usuarioDTO.setId(20L);
        pedidoDTO.setUsuarioDTO(usuarioDTO);

        Usuario outroUsuario = new Usuario();
        outroUsuario.setId(20L);
        outroUsuario.setEndereco(enderecoUsuario); // Garantir que usuário novo tb tem endereço se necessário
        when(usuarioRepository.findById(20L)).thenReturn(Optional.of(outroUsuario));

        // Ao salvar, retornamos o mesmo pedido com status definido
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedido);

        PedidoDTO resultado = pedidoService.atualizarPedido(1L, pedidoDTO);
        verify(pedidoRepository, times(1)).save(pedido);
        Assertions.assertNotNull(resultado);
    }

    @Test
    void atualizarPedido_naoEncontrado() {
        when(pedidoRepository.findById(1L)).thenReturn(Optional.empty());
        RuntimeException thrown = Assertions.assertThrows(RuntimeException.class, () -> {
            pedidoService.atualizarPedido(1L, pedidoDTO);
        });
        Assertions.assertEquals("Pedido não encontrado", thrown.getMessage());
        verify(pedidoRepository, never()).save(any());
    }

    @Test
    void deletarPedido_encontrado() {
        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));
        boolean resultado = pedidoService.deletarPedido(1L);
        Assertions.assertTrue(resultado);
        verify(pedidoRepository, times(1)).deleteById(1L);
    }

    @Test
    void deletarPedido_naoEncontrado() {
        when(pedidoRepository.findById(1L)).thenReturn(Optional.empty());
        boolean resultado = pedidoService.deletarPedido(1L);
        Assertions.assertFalse(resultado);
        verify(pedidoRepository, never()).deleteById(anyLong());
    }

    @Test
    void atualizarStatusPedido_sucesso() {
        UpdateStatusDTO updateStatusDTO = new UpdateStatusDTO();
        updateStatusDTO.setStatusPedido(StatusPedido.SHIPPED);

        // Garantir que pedido tem status inicial
        pedido.setStatusPedido(StatusPedido.PENDING_PAYMENT);
        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedido); // retorna pedido com status atualizado

        PedidoDTO resultado = pedidoService.atualizarStatusPedido(1L, updateStatusDTO);
        verify(pedidoRepository, times(1)).save(pedido);
        Assertions.assertNotNull(resultado);
        Assertions.assertEquals(StatusPedido.SHIPPED.name(), resultado.getStatusPedido());
    }

    @Test
    void atualizarStatusPedido_naoEncontrado() {
        UpdateStatusDTO updateStatusDTO = new UpdateStatusDTO();
        when(pedidoRepository.findById(1L)).thenReturn(Optional.empty());
        EntityNotFoundException thrown = Assertions.assertThrows(EntityNotFoundException.class, () -> {
            pedidoService.atualizarStatusPedido(1L, updateStatusDTO);
        });
        Assertions.assertEquals("Pedido não encontrado", thrown.getMessage());
    }

    @Test
    void obterPedidosPorUsuario_sucesso() {
        Pedido outroPedido = new Pedido();
        outroPedido.setId(2L);
        outroPedido.setStatusPedido(StatusPedido.PENDING_PAYMENT); // definir status
        when(pedidoRepository.findAllByUsuarioIdOrderByIdDesc(10L)).thenReturn(Arrays.asList(pedido, outroPedido));

        List<PedidoDTO> resultado = pedidoService.obterPedidosPorUsuario(10L);
        Assertions.assertEquals(2, resultado.size());
    }

    @Test
    void obterPedidosPorUsuario_semPedidos() {
        when(pedidoRepository.findAllByUsuarioIdOrderByIdDesc(10L)).thenReturn(Collections.emptyList());
        List<PedidoDTO> resultado = pedidoService.obterPedidosPorUsuario(10L);
        Assertions.assertTrue(resultado.isEmpty());
    }
}
