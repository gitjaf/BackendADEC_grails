include "institucionFixtures"

build{
	cursoAprobado1(org.sigma.code.CursoAprobado, fechaFinalizacion: new Date(), institucion: institucion1, curso: curso1)
	cursoAprobado2(org.sigma.code.CursoAprobado, fechaFinalizacion: new Date(), institucion: institucion2, curso: curso2)
	cursoAprobado3(org.sigma.code.CursoAprobado, fechaFinalizacion: new Date(), institucion: institucion3, curso: curso3)
	cursoAprobado4(org.sigma.code.CursoAprobado, fechaFinalizacion: new Date(), institucion: institucion4, curso: curso4)
	cursoAprobado5(org.sigma.code.CursoAprobado, fechaFinalizacion: new Date(), institucion: institucion5, curso: curso5)
}