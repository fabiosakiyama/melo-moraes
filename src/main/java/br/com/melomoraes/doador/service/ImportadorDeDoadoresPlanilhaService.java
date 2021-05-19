package br.com.melomoraes.doador.service;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import br.com.melomoraes.client.GoogleClient;
import br.com.melomoraes.doador.model.Doador;
import br.com.melomoraes.doador.model.Endereco;
import br.com.melomoraes.doador.repository.DoadorRepository;

@Service
public class ImportadorDeDoadoresPlanilhaService {
	
	private static final String SEMANA = "Semana";

	private static final String CONTATO = "Contato";

	private static final String OBS = "Obs";

	private static final String COMPLEMENTO = "Complemento";

	private static final String PLACE_ID = "PlaceId";

	private static final String NUMERO = "Numero";

	private static final String BAIRRO = "Bairro";

	private static final String RUA = "Rua";

	private static final String QUANT = "Quant";

	private static final String NOME = "Nome";

	private DoadorRepository repository;
	
	private GoogleClient client;
	
	private static Map<String, Integer> mapaNomeColunaIndex = new HashMap<>();
	
	static {
		mapaNomeColunaIndex.put(NOME, 0);
		mapaNomeColunaIndex.put(QUANT, 1);
		mapaNomeColunaIndex.put(CONTATO, 2);
		mapaNomeColunaIndex.put(SEMANA, 3);
		mapaNomeColunaIndex.put(RUA, 4);
		mapaNomeColunaIndex.put(BAIRRO, 5);
		mapaNomeColunaIndex.put(NUMERO, 6);
		mapaNomeColunaIndex.put(COMPLEMENTO, 7);
		mapaNomeColunaIndex.put(OBS, 8);
		mapaNomeColunaIndex.put(PLACE_ID, 9);
	}
	
	@PostConstruct
	public void carregaDoadoresIniciais() throws IOException {
		InputStream is = new FileInputStream("src/main/resources/carga-inicial-doadores.xlsx");
		this.importaDoadoresViaPlanilha(is);
	}
	
	public ImportadorDeDoadoresPlanilhaService(DoadorRepository repository, GoogleClient client) {
		this.repository = repository;
		this.client = client;
	}

	public long importaDoadoresViaPlanilha(InputStream is) throws IOException {
		Workbook workbook = new XSSFWorkbook(is);
		int doadoresImportados = 0;
		int rowIndex = 1;
		for(int sheetIndex = 0; sheetIndex < workbook.getNumberOfSheets(); sheetIndex++) {
			Sheet sheet = workbook.getSheetAt(sheetIndex);
			rowIndex = 1;
			Row row = sheet.getRow(rowIndex);
			String nome = row.getCell(0).getStringCellValue();
			while (StringUtils.hasLength(nome)) {

				Cell cellRua = row.getCell(mapaNomeColunaIndex.get(RUA));
				Assert.notNull(cellRua, "Rua é obrigatória");
				String rua = cellRua.getStringCellValue();
				Cell cellBairro = row.getCell(mapaNomeColunaIndex.get(BAIRRO));
				Assert.notNull(cellBairro, "Bairro é obrigatório");
				String bairro = cellBairro.getStringCellValue();
				Cell cellNumero = row.getCell(mapaNomeColunaIndex.get(NUMERO));
				Assert.notNull(cellNumero, "Número é obrigatório");
				String numero = Integer.toString((int) cellNumero.getNumericCellValue());
				Cell cellPlaceId = row.getCell(mapaNomeColunaIndex.get(PLACE_ID));
				String placeId = (cellPlaceId != null ? cellPlaceId.getStringCellValue() : null);
				Cell cellComplemento = row.getCell(mapaNomeColunaIndex.get(COMPLEMENTO));
				String complemento = (cellComplemento != null ? cellComplemento.getStringCellValue() : null);
				Cell cellObs = row.getCell(mapaNomeColunaIndex.get(OBS));
				String obs = (cellObs != null ? cellObs.getStringCellValue() : null);
				Cell contatoObs = row.getCell(mapaNomeColunaIndex.get(CONTATO));
				String contato = (contatoObs != null ? contatoObs.getStringCellValue() : null);
				Cell cellQuantidade = row.getCell(mapaNomeColunaIndex.get(QUANT));
				Assert.notNull(cellQuantidade, "Quantidade é obrigatória");
				Assert.isTrue(cellQuantidade.getNumericCellValue() > 0, "Quantidade é precisa ser maior que zero");
				Double quantidade = cellQuantidade.getNumericCellValue();
				Cell cellSemana = row.getCell(mapaNomeColunaIndex.get(SEMANA));
				Assert.notNull(cellSemana, "Semana é obrigatória");
				Assert.isTrue(
						(int) cellSemana.getNumericCellValue() >= 1 && (int) cellSemana.getNumericCellValue() <= 4,
						"Semana precisa estar entre 1 e 4");
				Integer semana = (int) cellSemana.getNumericCellValue();

				if (!StringUtils.hasLength(placeId)) {
					placeId = client
							.buscaPlaceIdDeUmEndereco(rua + ", " + numero + ", " + bairro + ", Pindamonhangaba");
				}

				Endereco endereco = new Endereco(rua, bairro, numero, placeId.trim(), complemento, obs);
				Doador doador = new Doador(nome, contato, quantidade, semana, endereco);
				repository.save(doador);

				rowIndex++;
				row = sheet.getRow(rowIndex);
				if (row == null || row.getCell(0) == null) {
					break;
				}
				nome = row.getCell(0).getStringCellValue();
			}
			doadoresImportados+= rowIndex;
		}
		workbook.close();
		return doadoresImportados;
	}
}
