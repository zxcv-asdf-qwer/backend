package co.kr.compig.api.domain.file;

import co.kr.compig.api.domain.code.FileType;
import co.kr.compig.api.domain.code.IsYn;
import co.kr.compig.api.domain.code.UseYn;
import co.kr.compig.api.domain.code.converter.FileTypeConverter;
import co.kr.compig.global.embedded.CreatedAndUpdated;
import co.kr.compig.api.domain.board.Board;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table
@SequenceGenerator(
	name = "system_file_seq_gen", //시퀀스 제너레이터 이름
	sequenceName = "system_file_seq", //시퀀스 이름
	initialValue = 1, //시작값
	allocationSize = 1 //메모리를 통해 할당 할 범위 사이즈
)
public class SystemFile {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "system_file_seq_gen")
	@Column(name = "system_file_id")
	private Long id; // 파일 id

	@Column
	private String filePath; // 파일 경로

	@Column
	private String fileNm; // 파일 이름

	@Column(length = 10)
	@Convert(converter = FileTypeConverter.class)
	private FileType fileType; // 파일 타입

	@Column
	private String fileExtension; // 파일 확장자명

	@Column(length = 1)
	@Enumerated(EnumType.STRING)
	@Builder.Default
	private IsYn latestYn = IsYn.Y; // 최신 여부

	@Column(length = 1)
	@Enumerated(EnumType.STRING)
	@Builder.Default
	private UseYn useYn = UseYn.Y; // 사용 여부

	/* =================================================================
	 * Domain mapping
	   ================================================================= */
	@Builder.Default
	@JoinColumn(name = "board_id", nullable = false, foreignKey = @ForeignKey(name = "fk01_system_file"))
	@ManyToOne(fetch = FetchType.LAZY)
	private Board board = new Board();

	/* =================================================================
	 * Default columns
	   ================================================================= */
	@Embedded
	@Builder.Default
	private CreatedAndUpdated createdAndModified = new CreatedAndUpdated();
}

