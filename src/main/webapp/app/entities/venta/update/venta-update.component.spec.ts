import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { VentaService } from '../service/venta.service';
import { IVenta, Venta } from '../venta.model';

import { VentaUpdateComponent } from './venta-update.component';

describe('Venta Management Update Component', () => {
  let comp: VentaUpdateComponent;
  let fixture: ComponentFixture<VentaUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let ventaService: VentaService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [VentaUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(VentaUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(VentaUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    ventaService = TestBed.inject(VentaService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const venta: IVenta = { id: 456 };

      activatedRoute.data = of({ venta });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(venta));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Venta>>();
      const venta = { id: 123 };
      jest.spyOn(ventaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ venta });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: venta }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(ventaService.update).toHaveBeenCalledWith(venta);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Venta>>();
      const venta = new Venta();
      jest.spyOn(ventaService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ venta });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: venta }));
      saveSubject.complete();

      // THEN
      expect(ventaService.create).toHaveBeenCalledWith(venta);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Venta>>();
      const venta = { id: 123 };
      jest.spyOn(ventaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ venta });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(ventaService.update).toHaveBeenCalledWith(venta);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
